package dev.tools.screenlogger

import android.text.Spannable
import android.text.TextUtils
import android.text.style.StyleSpan

object StringUtil {
    fun highlightText(content: String, highlightContent: String, styleRes: Int): CharSequence {
        var result: CharSequence = content
        if (!TextUtils.isEmpty(highlightContent)) {
            val startHighlightIndex = content.indexOf(highlightContent)
            if (startHighlightIndex == -1) {
                //  highlight text not found
                return content
            }
            val stopIndex = startHighlightIndex + highlightContent.length
            val spanText = Spannable.Factory.getInstance().newSpannable(content)
            spanText.setSpan(
                StyleSpan(styleRes),
                startHighlightIndex,
                stopIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            result = spanText
        }
        return result
    }

    fun beautifyJsonString(text: String): String {
        val json = StringBuilder()
        var indentString = ""
        var previousLetter = 0.toChar()
        var nextLetter = 0.toChar()
        for (i in 0 until text.length) {
            val letter = text[i]
            nextLetter = if (i < text.length - 1) {
                text[i + 1]
            } else {
                0.toChar()
            }
            when (letter) {
                '{' -> {
                    if (nextLetter == '}') {
                        json.append(indentString + letter)
                        break
                    }
                    json.append(indentString + letter + "\n")
                    indentString = indentString + "\t"
                    json.append(indentString)
                }

                '[' -> {
                    if (nextLetter == ']') {
                        json.append(
                            """
                                
                                $indentString$letter
                                """.trimIndent()
                        )
                        break
                    }
                    json.append(
                        """
                            
                            $indentString$letter
                            
                            """.trimIndent()
                    )
                    indentString = indentString + "\t"
                    json.append(indentString)
                }

                '}' -> {
                    if (previousLetter == '{') {
                        json.append(letter)
                        break
                    }
                    if (previousLetter == '[') {
                        json.append(letter)
                        break
                    }
                    indentString = indentString.replaceFirst("\t".toRegex(), "")
                    json.append(
                        """
                            
                            $indentString$letter
                            """.trimIndent()
                    )
                }

                ']' -> {
                    if (previousLetter == '[') {
                        json.append(letter)
                        break
                    }
                    indentString = indentString.replaceFirst("\t".toRegex(), "")
                    json.append(
                        """
                            
                            $indentString$letter
                            """.trimIndent()
                    )
                }

                ',' -> json.append(
                    """
                        $letter
                        $indentString
                        """.trimIndent()
                )

                else -> json.append(letter)
            }
            previousLetter = letter
        }

        return json.toString()
    }
}
