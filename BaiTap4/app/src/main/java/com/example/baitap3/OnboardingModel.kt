package com.example.baitap4

enum class OnboardingVisualType {
    CHECKLIST,
    CATEGORIES,
    NOTIFICATION
}

class OnboardingStep(
    val title: String,
    val description: String,
    val stepNumber: Int,
    val totalSteps: Int = 3,
    val buttonText: String,
    val visualType: OnboardingVisualType
)

object OnboardingData {
    val steps = listOf(
        OnboardingStep(
            title = "Mọi việc, gọn trong một chỗ",
            description = "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.",
            stepNumber = 1,
            buttonText = "Tiếp tục",
            visualType = OnboardingVisualType.CHECKLIST
        ),
        OnboardingStep(
            title = "Chia theo Học tập và Cuộc sống",
            description = "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.",
            stepNumber = 2,
            buttonText = "Tiếp tục",
            visualType = OnboardingVisualType.CATEGORIES
        ),
        OnboardingStep(
            title = "Nhắc đúng lúc",
            description = "Bật thông báo để UTH Todo nhắc bạn trước hạn chót.",
            stepNumber = 3,
            buttonText = "Bắt đầu ngay",
            visualType = OnboardingVisualType.NOTIFICATION
        )
    )
}
