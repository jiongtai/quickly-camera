package com.bayee.cameras.questionfeedback

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModelProvider
import com.bayee.cameras.R
import com.bayee.cameras.activity.base.BaseTitleActivity
import com.bayee.cameras.databinding.ActivityQuestionFeedBackBinding

class QuestionFeedBackActivity : BaseTitleActivity<ActivityQuestionFeedBackBinding>() {

    private lateinit var viewModel: QuestionFeedBackViewModel

    private var typeIndex = 1

    override fun initViews() {
        super.initViews()
        binding.questionfeedbackToolbar.toolbarTextCenter.text = "问题反馈"
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun initDatum() {
        super.initDatum()
        viewModel =
            ViewModelProvider(this).get(QuestionFeedBackViewModel::class.java)
        initViewModel(viewModel)
    }

    override fun initListeners() {
        super.initListeners()
        clickBtnChanged()
        clickCommit()
    }

    private fun clickCommit() {
        binding.questionBtnCommit.setOnClickListener {
            val content = binding.questionFeedbackEdittext.text.toString()
            viewModel.sendFeedback(typeIndex, content)
        }
    }

    private fun clickBtnChanged() {
        binding.btn1UseQuestion.isChecked = true
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.btn1_useQuestion -> {
                    typeIndex = 1
                }

                R.id.btn2_suggestion -> {
                    typeIndex = 2
                }

                R.id.btn3_otherQuestion -> {
                    typeIndex = 3
                }
            }
            Log.d(TAG, "clickBtnChanged: $typeIndex")
        }
    }


    companion object {
        private const val TAG = "QuestionFeedBackActivity"
    }


}