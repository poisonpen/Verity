package com.example.chapter

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chapter.databinding.FragmentChapterListBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

import java.util.*

private const val TAG = "ChapterListFragment"

class ChapterListFragment : Fragment() {
    private var _binding: FragmentChapterListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding [null reference]"
        }
    private val chapterListViewModel: ChapterListViewModel by viewModels()
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChapterListBinding.inflate(inflater, container, false)
        binding.chapterRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // TODO
                // val chapters = chapterListViewModel.loadChapters()
                chapterListViewModel.chapters.collect { chapters ->
                    binding.chapterRecyclerView.adapter =
                        ChapterListAdapter(chapters) { chapterId ->
                            findNavController().navigate(
                                ChapterListFragmentDirections.showChapterDetail(chapterId)
                            )
                        }
                }
            }
//            viewLifecycleOwner.lifecycleScope.launch {
//                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    chapterListViewModel.chapters.collect() { chapters ->
//                        binding.chapterRecyclerView.adapter =
//                            ChapterListAdapter(chapters) {
//                                findNavController().navigate(
//                                    R.id.show_chapter_detail
//                                )
//                            }
//                    }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_chapter_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_chapter -> {
                showNewChapter()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    private fun showNewChapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newChapter = Chapter (
                id = UUID.randomUUID(),
                strangeText = "",
                translationText = "",
                language = "",
                comment = "",
                lastUpdated = Date()
            )
            chapterListViewModel.addChapter(newChapter)
            findNavController().navigate(
                ChapterListFragmentDirections.showChapterDetail(newChapter.id)
            )
        }
    }
}

// FOLLOW STEPS AND MAKE MENU ADD BUTTON FUNCTIONAL.
