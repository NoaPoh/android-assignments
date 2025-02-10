package com.example.brook.model

import android.graphics.Bitmap
import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.brook.model.AppLocalDb.getAppDb
import com.example.brook.model.Model.LoadingState
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Model private constructor() {
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel = FirebaseModel()
    private val mAuth = FirebaseAuth.getInstance()

    var localDb: AppLocalDbRepository = getAppDb()

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    @JvmField
    val EventPostsListLoadingState: MutableLiveData<LoadingState?> = MutableLiveData<LoadingState?>(
        LoadingState.NOT_LOADING
    )
    private var postList: LiveData<MutableList<Post?>?>? = null
    private var myPostList: LiveData<MutableList<Post?>?>? = null

    fun interface Listener<T> {
        fun onComplete(data: T?)
    }


    fun getAllPosts(): LiveData<MutableList<Post?>?>? {
        if (postList == null) {
            postList = localDb.PostDao()!!.getAll()
            refreshAllPosts()
        }
        return postList
    }

    fun getUserPosts(): LiveData<MutableList<Post?>?>? {
        val email = getConnectedUser()
        if (myPostList == null || myPostList!!.getValue() == null) {
            myPostList = localDb.PostDao()!!.getPostsByEmail(email)
        }
        return myPostList
    }

    fun refreshAllPosts() {
        EventPostsListLoadingState.setValue(LoadingState.LOADING)
        // get local last update
        val localLastUpdate = if (postList!!.getValue() == null) 0L else Post.getLocalLastUpdate()
        // get all updated recorde from firebase since local last update
        firebaseModel.getAllPostsSince(localLastUpdate, Model.Listener { list: MutableList<Post>? ->
            executor.execute(Runnable {
                Log.d("TAG", " firebase return : " + list!!.size)
                var time = localLastUpdate
                for (post in list) {
                    // insert new records into ROOM
                    localDb.PostDao()!!.insertAll(post)
                    if (time < Post.getLocalLastUpdate()) {
                        time = Post.getLocalLastUpdate()
                    }
                }
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                // update local last update
                Post.setLocalLastUpdated(time)
                EventPostsListLoadingState.postValue(LoadingState.NOT_LOADING)
            })
        })
    }

    fun addPost(post: Post, listener: Listener<Void?>) {
        firebaseModel.addPost(post, Model.Listener { Void: Post? ->
            refreshAllPosts()
            listener.onComplete(null)
        })
    }

    fun uploadImage(name: String?, bitmap: Bitmap, listener: Listener<String?>) {
        firebaseModel.uploadImage(name, bitmap, listener)
    }

    fun getUserById(email: String, listener: Listener<User?>) {
        firebaseModel.getUserById(email, listener)
    }

    fun login(email: String, password: String, listener: Listener<Void?>) {
        firebaseModel.login(email, password, listener)
    }

    fun signUp(user: User, password: String, listener: Listener<User?>?) {
        firebaseModel.signUp(user, password, listener)
    }

    fun getConnectedUser(): String? {
        return firebaseModel.getConnectedUser()
    }

    fun editInfo(user: User, listener: Listener<User?>?) {
        firebaseModel.addUser(user, listener)
    }

    fun getPostById(id: String?, listener: Listener<Post?>?) {
        firebaseModel.getPostById(id, listener)
    }

    fun logout() {
        firebaseModel.logout()
    }

    companion object {
        private val _instance = Model()

        @JvmStatic
        fun instance(): Model {
            return _instance
        }
    }
}
