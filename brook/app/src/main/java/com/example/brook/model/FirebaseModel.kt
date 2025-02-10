package com.example.brook.model

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.LinkedList

class FirebaseModel internal constructor() {
    var db: FirebaseFirestore
    var storage: FirebaseStorage
    var mAuth: FirebaseAuth

    init {
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        db.setFirestoreSettings(settings)
        storage = FirebaseStorage.getInstance()
        mAuth = FirebaseAuth.getInstance()
    }

    fun getAllPostsSince(since: Long, callback: Model.Listener<LinkedList<Post>?>) {
        db.collection(Post.COLLECTION)
            .whereGreaterThanOrEqualTo(Post.LAST_UPDATED, Timestamp(since, 0)).get()
            .addOnCompleteListener(OnCompleteListener { task: Task<QuerySnapshot>? ->
                val list = LinkedList<Post>()
                val post: Post? = null
                if (task!!.isSuccessful() && task.getResult() != null) {
                    val jsonList = task.getResult()
                    for (json in jsonList) {
                        val data: MutableMap<String?, Any?>? = json.getData()

                        if (data != null) {
                            list.add(Post.fromJson(data))
                        }
                    }
                }
                callback.onComplete(list)
            })
        // db.collection(Post.COLLECTION)
        // .whereGreaterThanOrEqualTo(Post.LAST_UPDATED, new Timestamp(since,0))
        // .get()
        // .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        // @Override
        // public void onComplete(@NonNull Task<QuerySnapshot> task) {
        // List<Post> list = new LinkedList<>();
        // if (task.isSuccessful()){
        // QuerySnapshot jsonList = task.getResult();
        // for (DocumentSnapshot json: jsonList){
        // Post st = Post.fromJson(json.getData());
        // list.add(st);
        // }
        // }
        // callback.onComplete(list);
        // }
        // });
    }

    fun addPost(post: Post, listener: Model.Listener<Post?>) {
        val postJson = post.toJson()
        db.collection(Post.COLLECTION)
            .document(post.getId())
            .set(postJson)
            .addOnSuccessListener(OnSuccessListener { unused: Void? -> listener.onComplete(post) })
            .addOnFailureListener(OnFailureListener { e: Exception? -> listener.onComplete(post) })
    }

    fun uploadImage(name: String?, bitmap: Bitmap, listener: Model.Listener<String?>) {
        val storageRef = storage.getReference()
        val imagesRef = storageRef.child("images/" + name + ".jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnFailureListener { exception ->
            listener.onComplete(null)
        }.addOnSuccessListener { taskSnapshot ->
            imagesRef.getDownloadUrl().addOnSuccessListener { uri ->
                listener.onComplete(uri.toString())
            }
        }
    }

    fun addUser(user: User, listener: Model.Listener<User?>?) {
        val userJson = user.toJson()
        db.collection(User.Companion.COLLECTION)
            .document(user.getEmail())
            .set(userJson)
            .addOnSuccessListener(OnSuccessListener { unused: Void? -> listener!!.onComplete(user) })
            .addOnFailureListener(OnFailureListener { e: Exception? -> listener!!.onComplete(user) })
    }

    fun signUp(user: User, password: String, listener: Model.Listener<User?>?) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
            .addOnCompleteListener(OnCompleteListener { task: Task<AuthResult?>? ->
                if (task!!.isSuccessful()) {
                    addUser(user, listener)
                }
            })
    }

    fun login(email: String, password: String, listener: Model.Listener<Void?>) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener { task: Task<AuthResult?>? ->
                if (task!!.isSuccessful()) {
                    listener.onComplete(null)
                }
            })
    }

    fun getUserById(email: String, listener: Model.Listener<User?>) {
        db.collection(User.Companion.COLLECTION)
            .document(email)
            .get()
            .addOnCompleteListener(OnCompleteListener { task: Task<DocumentSnapshot?>? ->
                var user: User? = null
                if (task!!.isSuccessful && task.result != null) {
                    val dataMap: MutableMap<String?, Any?> = task.result!!.data?.toMutableMap() as MutableMap<String?, Any?>
                    user = User.fromJson(dataMap)
                }

                listener.onComplete(user)
            })
    }

    fun getPostById(id: String, listener: Model.Listener<Post?>?) {
        db.collection(Post.COLLECTION)
            .document(id)
            .get()
            .addOnCompleteListener(OnCompleteListener { task: Task<DocumentSnapshot?> ->
                var post: Post = Post()
                if (task!!.isSuccessful() && task.getResult() != null) {
                    post = Post.fromJson(task.getResult()!!.getData() as MutableMap<String?, Any?>)
                }
                listener!!.onComplete(post)
            })
    }

    fun getConnectedUser(): String? {
        val firebaseUser = mAuth.getCurrentUser()

        return if (firebaseUser != null) firebaseUser.getEmail() else null
    }

    fun logout() {
        mAuth.signOut()
    }
}
