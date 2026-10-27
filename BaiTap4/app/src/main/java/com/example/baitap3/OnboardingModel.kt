package com.example.baitap4

abstract class BasePageModel(
    val id: Int,
    val title: String,
    val description: String
) {
    abstract fun getPageType(): String
}

enum class OnboardingVisualType {
    CHECKLIST,
    CATEGORIES,
    NOTIFICATION
}

open class OnboardingStep(
    id: Int,
    title: String,
    description: String,
    val stepNumber: Int,
    val totalSteps: Int,
    val buttonText: String,
    val visualType: OnboardingVisualType
) : BasePageModel(id, title, description) {

    override fun getPageType(): String = "OnboardingStep_$stepNumber"
}

class OnboardingRepository {
    private val steps: List<OnboardingStep> = listOf(
        OnboardingStep(
            id = 1,
            title = "Mọi việc, gọn trong một chỗ",
            description = "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.",
            stepNumber = 1,
            totalSteps = 3,
            buttonText = "Tiếp tục",
            visualType = OnboardingVisualType.CHECKLIST
        ),
        OnboardingStep(
            id = 2,
            title = "Chia theo Học tập và Cuộc sống",
            description = "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.",
            stepNumber = 2,
            totalSteps = 3,
            buttonText = "Tiếp tục",
            visualType = OnboardingVisualType.CATEGORIES
        ),
        OnboardingStep(
            id = 3,
            title = "Nhắc đúng lúc",
            description = "Bật thông báo để UTH Todo nhắc bạn trước hạn chót.",
            stepNumber = 3,
            totalSteps = 3,
            buttonText = "Bắt đầu ngay",
            visualType = OnboardingVisualType.NOTIFICATION
        )
    )

    fun getStepByNumber(stepNumber: Int): OnboardingStep? {
        return steps.find { it.stepNumber == stepNumber }
    }
}
