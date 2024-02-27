package com.example.mysql

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationsql.db.MyDbManager
import com.example.mysql.databinding.EditActivityBinding
import com.example.mysql.db.MyIntentConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditActivity : AppCompatActivity() {
    var id = 0
    var isEditState = false
    val imageRequestCode = 10
    var tempImageUri = "empty"
    val myDbManager = MyDbManager(this)

    private lateinit var binding: EditActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMyIntents()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode)

            binding.imageView.setImageURI(data?.data)
        tempImageUri = data?.data.toString()
        contentResolver.takePersistableUriPermission(
            data?.data!!,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )


    }

    fun onClickAddImage(view: View) {
        binding.mainImageLayout.visibility = View.VISIBLE
        binding.fbAddImage.visibility = View.GONE
    }

    fun onClickDeleteImage(view: View) {
        binding.mainImageLayout.visibility = View.GONE
        binding.fbAddImage.visibility = View.VISIBLE
        tempImageUri = "empty"
    }

    fun onClickChooseImage(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)

    }

    fun onClickSave(view: View) {
        val myTitle = binding.etTitle.text.toString()
        val myDesc = binding.etDescription.text.toString()

        if (myTitle != "" && myDesc != "") {
            CoroutineScope(Dispatchers.Main).launch {
                if (isEditState) {
                    myDbManager.updateItemInDb(myTitle, myDesc, tempImageUri, id, getCurrentTime() )
                } else {
                    myDbManager.insertToDb(myTitle, myDesc, tempImageUri, getCurrentTime())

                }
                finish()
            }

        }
    }

    fun onEditEnable(view: View) {
        binding.etTitle.isEnabled = true
        binding.etDescription.isEnabled = true
        binding.fbEdit.visibility = View.GONE
        binding.fbAddImage.visibility = View.VISIBLE
        if(tempImageUri =="empty") return
        binding.fbEdit.visibility = View.VISIBLE
        binding.btnDeleteImage.visibility = View.VISIBLE


    }

    private fun getMyIntents() {
        binding.fbEdit.visibility = View.GONE
        val i = intent
        if (i != null) {
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {

                binding.fbAddImage.visibility = View.GONE
                binding.etTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                isEditState = true
                binding.etTitle.isEnabled = false
                binding.etDescription.isEnabled = false
                binding.fbEdit.visibility = View.VISIBLE
                binding.etDescription.setText(i.getStringExtra(MyIntentConstants.I_DESK_KEY))
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {

                    binding.mainImageLayout.visibility = View.VISIBLE
                    tempImageUri = i.getStringExtra(MyIntentConstants.I_URI_KEY)!!
                    binding.imageView.setImageURI(Uri.parse(tempImageUri))
                    binding.imButtonEditImage.visibility = View.GONE
                    binding.btnDeleteImage.visibility = View.GONE
                }
            }
        }
    }

    private fun getCurrentTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }
}