package provider_widget_generator.action

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidatorEx

class SimpleClassNameInputValidator : InputValidatorEx {
    override fun checkInput(input: String): Boolean {
        return getErrorText(input) == null
    }

    override fun canClose(p0: String?): Boolean {
        return true
    }

    override fun getErrorText(input: String): String? {
        if (input.contains(".") || input.contains(" ") || input.toLowerCase() != input) {
            return "Must only contain lowercase characters and underscores"
        }

        return null
    }
}