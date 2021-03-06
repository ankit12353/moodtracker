package com.android.mood.fragment

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.mood.R
import com.android.mood.activity.SplashActivity
import com.android.mood.helper.Constant
import com.android.mood.helper.Constant.LANGUAGE
import com.android.mood.helper.LocaleHelper
import kotlinx.android.synthetic.main.fragment_more_navigationbar.view.*
import java.io.*
import java.util.*


class MoreFragmentBottomNavigation : Fragment() {
    private var v: View? = null
    private var mContext: Context? = null
    private var ivUser : ImageView?= null
    private var ivEdit : ImageView?= null
    private var etUserName : EditText ?= null
    private var tvUserName : TextView ?= null
    private var sp : SharedPreferences?= null
    private var rlChangeLanguage: RelativeLayout ?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_more_navigationbar, container, false)
        init()

        v!!.findViewById<CardView>(R.id.select_image).setOnClickListener { selectImage() }
        ivEdit!!.setOnClickListener { editUserName() }
        rlChangeLanguage!!.setOnClickListener { openLanguagePopup()}

        return v
    }
    private fun init(){
        ivUser = v!!.findViewById(R.id.iv_user)
        ivEdit = v!!.findViewById(R.id.iv_edit_username)
        etUserName = v!!.findViewById(R.id.et_user_name)
        tvUserName = v!!.findViewById(R.id.tv_user_name)
        rlChangeLanguage = v!!.findViewById(R.id.rl_change_language)
        sp = activity!!.getSharedPreferences(Constant.PREFERENCE,Context.MODE_PRIVATE)
        val file = Utility.readImageFromInternalStorage(activity!!)
        if(file!= null) ivUser!!.setImageDrawable(Drawable.createFromPath(file.toString())) else ivUser!!.setImageResource(R.drawable.upload)

        val name = sp!!.getString(userName,"")
        if(name != null && name !="") tvUserName!!.text=name else tvUserName!!.text= resources.getString(R.string.text_enter_username)
    }

    private fun openLanguagePopup(){
        val popupMenu = PopupMenu(mContext, rlChangeLanguage!!)
        val menu = popupMenu.menu
        popupMenu.menuInflater.inflate(R.menu.language_menu, menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.language_default -> {
                    changeLanguage(Locale.getDefault().language)
                }
                R.id.language_hindi -> {
                    changeLanguage("hi")
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun changeLanguage(language : String){
        LocaleHelper.setLocale(mContext!!,language)
        sp!!.edit().putString(LANGUAGE,language).apply()
        activity!!.recreate()
    }

    private fun editUserName() {
        if (etUserName!!.visibility == View.GONE) {
            etUserName!!.visibility = View.VISIBLE
            etUserName!!.requestFocus()
            tvUserName!!.visibility = View.GONE
            ivEdit!!.setImageResource(R.drawable.ic_tick)
        } else {
            val name = etUserName!!.text.trim().toString()
            if(name != "") {
                sp!!.edit().putString(userName,name).apply()
                tvUserName!!.text = name
            }
            (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(v!!.windowToken,0)
            etUserName!!.visibility = View.GONE
            tvUserName!!.visibility = View.VISIBLE
            ivEdit!!.setImageResource(R.drawable.ic_edit)
            etUserName!!.setText("")
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
            cameraResult(data)
        } else if(requestCode == galleryCode){
            galleryResult(data)
        }
    }

    //camera and gallery results
    private fun cameraResult(data: Intent?){
        val bytes = ByteArrayOutputStream()
        if(data != null) {
            var image : Bitmap?= null
            try {
                image = data.extras!!.get("data") as Bitmap
                image.compress(Bitmap.CompressFormat.JPEG,90,bytes)
                val destination = File(Environment.getExternalStorageDirectory(),System.currentTimeMillis().toString() + ".jpg")
                val fo: FileOutputStream
                destination.createNewFile()
                fo = FileOutputStream(destination)
                fo.write(bytes.toByteArray())
                fo.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Utility.saveImageToInternalStorage(image!!,activity!!)
            ivUser!!.setImageBitmap(image)
        }
    }
    private fun galleryResult(data: Intent?){
        var image: Bitmap? = null
        if (data != null) {
            try {
                image = MediaStore.Images.Media.getBitmap(mContext!!.contentResolver, data.data!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Utility.saveImageToInternalStorage(image!!,activity!!)
            ivUser!!.setImageBitmap(image)
        }
    }

    private object Utility {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        fun checkPermission(context: Context?): Boolean {
            val currentAPIVersion = Build.VERSION.SDK_INT
            return if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context!!,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((context as Activity?)!!,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        val alertBuilder = AlertDialog.Builder(context)
                        alertBuilder.setCancelable(true)
                        alertBuilder.setTitle(context.getString(R.string.text_permission_necessary))
                        alertBuilder.setMessage(context.getString(R.string.text_external_permission_necessary))
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

        fun saveImageToInternalStorage (bitmap: Bitmap,activity : Activity){
            val cw = ContextWrapper(activity.applicationContext)
            val directory: File = cw.getDir(imageDirectory, Context.MODE_PRIVATE)
            val file = File(directory, "$userImageName.jpg")
            Log.d("path", file.toString())
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun readImageFromInternalStorage(activity: Activity) : File?{
            val cw = ContextWrapper(activity.applicationContext)
            val directory = cw.getDir(imageDirectory,Context.MODE_PRIVATE)
            val file =  File(directory,"$userImageName.jpg")
            if(file.exists()){
                return file
            }
            return null
        }
    }

    companion object {
        private const val cameraCode : Int = 1
        private const val galleryCode : Int = 2
        private const val userName : String="name"
        private const val userImageName : String="cameraImage"
        private const val imageDirectory : String="imageDir"
    }
}