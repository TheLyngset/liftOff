package no.uio.ifi.in2000.team_17.ui.judicial_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.ui.Background
import no.uio.ifi.in2000.team_17.ui.thresholds.InfoSection
import kotlin.math.min

@Composable
fun JudicialScreen(modifier: Modifier, windowSizeClass: WindowSizeClass?) {
    Background()
    if (windowSizeClass == null || windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact) {
        LazyColumn(
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.legal),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                )
                LegalCard(
                    stringResource(R.string.ippcTitle),
                    stringResource(R.string.ippc_description),
                    stringResource(R.string.ippc_readMore),
                    listOf("IPPC Avinor"),
                    listOf("https://www.ippc.no/ippc/index.jsp")
                )
                LegalCard(
                    stringResource(R.string.model_rocket_safety_code_title),
                    stringResource(R.string.model_rocket_safety_code_description),
                    stringResource(R.string.model_rocket_safety_code_readMore),
                    listOf("Safety Code NAR"),
                    listOf("https://nar.org/safety-information/model-rocket-safety-code/")
                )
            }
        }
    } else {
        Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.legal),
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp
            )
            LazyRow(
                modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    LegalCard(
                        stringResource(R.string.ippcTitle),
                        stringResource(R.string.ippc_description),
                        stringResource(R.string.ippc_readMore),
                        listOf("IPPC Avinor"),
                        listOf("https://www.ippc.no/ippc/index.jsp")
                    )
                    LegalCard(
                        stringResource(R.string.model_rocket_safety_code_title),
                        stringResource(R.string.model_rocket_safety_code_description),
                        stringResource(R.string.model_rocket_safety_code_readMore),
                        listOf("Safety Code NAR"),
                        listOf("https://nar.org/safety-information/model-rocket-safety-code/")
                    )
                }
            }
        }
    }

}


@Composable
fun LegalCard(
    infoTitle: String,
    infoDesc: String,
    linkFullText: String,
    listLinkText: List<String>,
    hyperlinks: List<String>
) {
    BoxWithConstraints {
        val width = min(600f, maxWidth.value)
        Card(
            modifier = Modifier

                .padding(16.dp)
                .width(width = width.dp),

            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = Color.Unspecified,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledContentColor = Color.Unspecified
            )
        ) {


            InfoSection(title = infoTitle, description = infoDesc)
            HyperlinkText(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp),
                fullText = linkFullText,
                linkText = listLinkText,
                hyperlinks = hyperlinks
            )
        }
    }
}

//Lag hyperlink i tekst følger tutorial fra: https://www.youtube.com/watch?v=-fouArUd56I
@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color = Color.Blue,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    hyperlinks: List<String> = listOf(),
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        linkText.forEachIndexed { index, link ->
            val startIndex = fullText.indexOf(link)
            val endIndex = startIndex + link.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = "URL",
                annotation = hyperlinks[index],
                start = startIndex,
                end = endIndex
            )
        }
        addStyle(
            style = SpanStyle(
                fontSize = fontSize
            ),
            start = 0,
            end = fullText.length
        )
    }

    val uriHandler = LocalUriHandler.current

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = {
            annotatedString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}


@Composable
@Preview
fun preJS() {
    JudicialScreen(modifier = Modifier, null)
}