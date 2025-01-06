import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    var id: String,
    var name: String,
    var isChecked: Boolean = false
): Parcelable
