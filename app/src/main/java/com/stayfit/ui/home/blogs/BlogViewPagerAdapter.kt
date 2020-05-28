
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.stayfit.ui.home.blogs.PersonalBlogsFragment
import com.stayfit.ui.home.blogs.RecommendedBlogsFrament

class BlogViewPagerAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                PersonalBlogsFragment()
            }
            1 -> {
                RecommendedBlogsFrament()
            }
            else -> PersonalBlogsFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}