package com.example.brook

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import androidx.fragment.app.Fragment
import com.example.brook.model.Model
import com.example.brook.model.Model.Companion.instance
import com.example.brook.model.User
import com.squareup.picasso.Picasso

class ProfilePageFragment : Fragment() {
    var cameraLauncher: ActivityResultLauncher<Void?>? = null
    var galleryLauncher: ActivityResultLauncher<String?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        val imageButton = view.findViewById<View?>(R.id.editButton) as ImageButton
        val imageView = view.findViewById<View?>(R.id.profilePicture) as ImageView
        val galleryButton = view.findViewById<View?>(R.id.galleryButton) as ImageButton
        val cameraButton = view.findViewById<View?>(R.id.cameraButton) as ImageButton
        val saveButton = view.findViewById<View?>(R.id.saveButton) as Button
        val cancelButton = view.findViewById<View?>(R.id.cancelButton) as Button
        val userNameInput = view.findViewById<View?>(R.id.userNameInput) as EditText

        // Init the profile data
        val email = instance().getConnectedUser()
        instance().getUserById(email!!, Model.Listener { user: User? ->
            userNameInput.setText(user!!.getUserName())
            if (user.getAvatarUrl() != null && user.getAvatarUrl() != "") {
                Picasso.get()
                    .load(user.getAvatarUrl())
                    .into(imageView)
            }
        })

        imageButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val saveButton = view.findViewById<View?>(R.id.saveButton) as Button
                saveButton.setVisibility(View.VISIBLE)
                val cancelButton = view.findViewById<View?>(R.id.cancelButton) as Button
                cancelButton.setVisibility(View.VISIBLE)
                val userNameInput = view.findViewById<View?>(R.id.userNameInput) as EditText
                userNameInput.setEnabled(true)
                val galleryButton = view.findViewById<View?>(R.id.galleryButton) as ImageButton
                galleryButton.setVisibility(View.VISIBLE)
                val cameraButton = view.findViewById<View?>(R.id.cameraButton) as ImageButton
                cameraButton.setVisibility(View.VISIBLE)
            }
        })

        galleryLauncher = registerForActivityResult<String?, Uri?>(
            GetContent(),
            object : ActivityResultCallback<Uri?> {
                override fun onActivityResult(result: Uri?) {
                    if (result != null) {
                        imageView.setImageURI(result)
                    }
                }
            })

        galleryButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                galleryLauncher!!.launch("image/*")
            }
        })

        cameraLauncher = registerForActivityResult<Void?, Bitmap?>(
            TakePicturePreview(),
            object : ActivityResultCallback<Bitmap?> {
                override fun onActivityResult(result: Bitmap?) {
                    if (result != null) {
                        imageView.setImageBitmap(result)
                    }
                }
            })

        cameraButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                cameraLauncher!!.launch(null)
            }
        })

        saveButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                saveChanges(view)
            }
        })

        cancelButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                resetFragment(view)
            }
        })

        return view
    }

    private fun resetFragment(v: View) {
        val saveButton = v.findViewById<View?>(R.id.saveButton) as Button
        saveButton.setVisibility(View.INVISIBLE)
        val cancelButton = v.findViewById<View?>(R.id.cancelButton) as Button
        cancelButton.setVisibility(View.INVISIBLE)
        val userNameInput = v.findViewById<View?>(R.id.userNameInput) as EditText
        userNameInput.setEnabled(false)
        val galleryButton = v.findViewById<View?>(R.id.galleryButton) as ImageButton
        galleryButton.setVisibility(View.INVISIBLE)
        val cameraButton = v.findViewById<View?>(R.id.cameraButton) as ImageButton
        cameraButton.setVisibility(View.INVISIBLE)
    }

    fun saveChanges(v: View) {
        val userNameInput = v.findViewById<View?>(R.id.userNameInput) as EditText
        val userName = userNameInput.getText().toString().trim { it <= ' ' }

        val profilePicture = v.findViewById<View?>(R.id.profilePicture) as ImageView
        profilePicture.setDrawingCacheEnabled(true)
        profilePicture.buildDrawingCache()
        val bitmap = (profilePicture.getDrawable() as BitmapDrawable).getBitmap()

        val email = instance().getConnectedUser()
        val newUser = User(email!!, userName, "")
        instance().getUserById(email, Model.Listener { user: User? ->
            newUser.setAvatarUrl(user!!.getAvatarUrl())
        })

        instance().uploadImage(email, bitmap, Model.Listener { url: String? ->
            if (url != null) {
                newUser.setAvatarUrl(url)
            }
            instance().editInfo(newUser, Model.Listener { task: User? ->
                resetFragment(v)
            })
        })
    }
}