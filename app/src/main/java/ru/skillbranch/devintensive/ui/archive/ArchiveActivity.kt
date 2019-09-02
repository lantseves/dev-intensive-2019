package ru.skillbranch.devintensive.ui.archive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.setTextColor
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.custom.MyDividerItemDecorator
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.ArchiveViewModel
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class ArchiveActivity : AppCompatActivity() {
    private lateinit var viewModel:ArchiveViewModel
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        initToolbar()
        initViewModel()
        initViews()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel::class.java)
        viewModel.getArchiveChat().observe(this , Observer {chatAdapter.updateData(it)})
    }

    private fun initViews() {
        chatAdapter = ChatAdapter {
            Snackbar.make(rv_archive_list , "title ${it.title}" , Snackbar.LENGTH_LONG).show()
        }

        val divider = MyDividerItemDecorator(resources.getDrawable(R.drawable.divider , theme))
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter , R.drawable.ic_unarchive_black_24dp) {chat ->
            viewModel.restoreFromArchive(chat.id)
            val snkBar =Snackbar.make(rv_archive_list , "Востановить чат с ${chat.title.trim()} из архива?" , Snackbar.LENGTH_LONG)
                    .setAction("Отмена") {
                        viewModel.addToArchive(chat.id)
                    }
            val a = TypedValue()
            theme.resolveAttribute(R.attr.snackbarTextColor, a, true)
            snkBar.setTextColor(a.data)
            snkBar.view.background = resources.getDrawable(R.drawable.bg_snackbar , theme)
            snkBar.show()
        }

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_archive_list)
        with(rv_archive_list) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ArchiveActivity)
            addItemDecoration(divider)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.idle , R.anim.bottom_down)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
