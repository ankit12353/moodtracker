package com.android.mood.fragment

import android.Manifest
import android.R.attr
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.android.mood.R
import io.paperdb.Paper
import java.io.*


class MoreFragmentBottomNavigation : Fragment() {
    private var v: View? = null
    private var mContext: Context? = null
    private var ivUser : ImageView?= null
    private var ivEdit : ImageView?= null
    private var etUserName : EditText ?= null
    private var tvUserName : TextView ?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_more_navigationbar, container, false)
        init()

        v!!.findViewById<CardView>(R.id.select_image).setOnClickListener { selectImage() }
        ivEdit!!.setOnClickListener { editUserName() }

        return v
    }
    private fun init(){
        ivUser = v!!.findViewById(R.id.iv_user)
        ivEdit = v!!.findViewById(R.id.iv_edit_username)
        etUserName = v!!.findViewById(R.id.et_user_name)
        tvUserName = v!!.findViewById(R.id.tv_user_name)
        val name = Paper.book().read<String>(userName)
        if(name != "") tvUserName!!.text=name
    }

    private fun editUserName() {
        if (etUserName!!.visibility == View.GONE) {
            etUserName!!.visibility = View.VISIBLE
            etUserName!!.requestFocus()
            tvUserName!!.visibility = View.GONE
            ivEdit!!.setImageResource(R.drawable.ic_tick)
        } else {
            val name = etUserName!!.text.trim().toString()
            if(name == ""){
                etUserName!!.error = getString(R.string.text_required)
            } else {
                (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(v!!.windowToken,0)
                Paper.book().write(userName,name)
                tvUserName!!.text = name
                etUserName!!.visibility = View.GONE
                tvUserName!!.visibility = View.VISIBLE
                ivEdit!!.setImageResource(R.drawable.ic_edit)
            }
        }
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>(getString(R.string.text_take_photo), getString(R.string.text_choose_from_gallery), getString(R.string.text_cancel))
        val builder: AlertDialog.Builder = AlertDialog.Builder(mContext!!)
        builder.setTitle(getString(R.string.text_add_photo))
        builder.setItems(options) { dialog, item ->
            Utility.checkPermission(mContext!!)
            when {
                options[item] == getString(R.string.text_take_photo) -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent,cameraCode)
                }
                options[item] == getString(R.string.text_choose_from_gallery) -> {
                    val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, galleryCode)
                }
                options[item] == getString(R.string.text_cancel) -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == cameraCode){
            val bytes = ByteArrayOutputStream()
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            thumbnail.compress(Bitmap.CompressFormat.JPEG,90,bytes)
            val destination = File(Environment.getExternalStorageDirectory(),System.currentTimeMillis().toString() + ".jpg")
            val fo: FileOutputStream
            try {
                destination.createNewFile()
                fo = FileOutputStream(destination)
                fo.write(bytes.toByteArray())
                fo.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            ivUser!!.setImageBitmap(thumbnail)
        } else if(requestCode == galleryCode){
            var bitmap : Bitmap ?= null
            if(data!=null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(mContext!!.contentResolver,data.data!!)
                } catch (e : IOException){
                    e.printStackTrace()
                }
                ivUser!!.setImageBitmap(bitmap!!)
            }
        }
    }

    object Utility {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        fun checkPermission(context: Context?): Boolean {
            val currentAPIVersion = Build.VERSION.SDK_INT
            return if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context!!,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((context as Activity?)!!,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        val alertBuilder = AlertDialog.Builder(context)
                        alertBuilder.setCancelable(true)
                        alertBuilder.setTitle("Permission necessary")
                        alertBuilder.setMessage("External storage permission is necessary")
                        alertBuilder.setPositiveButton(android.R.string.yes) { dialog, which ->
                            ActivityCompat.requestPermissions((context as Activity?)!!,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                        }
                        val alert = alertBuilder.create()
                        alert.show()
                    } else {
                        ActivityCompat.requestPermissions((context as Activity?)!!,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    false
                } else {
                    true
                }
            } else {
                true
            }
        }
    }

    companion object {
        private const val cameraCode : Int = 1
        private const val galleryCode : Int = 2
        private const val userName : String="name"
        private const val userImage : String="image"
    }
}