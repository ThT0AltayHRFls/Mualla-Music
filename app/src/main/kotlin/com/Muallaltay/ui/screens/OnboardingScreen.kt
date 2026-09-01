/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Muallaltay.R

data class OnboardingState(
    val gender: String? = null,
    val favoriteGenres: List<String> = emptyList(),
    val favoriteArtists: List<String> = emptyList(),
    val currentStep: OnboardingStep = OnboardingStep.WELCOME,
)

enum class OnboardingStep {
    WELCOME,
    GENDER,
    GENRES,
    ARTISTS,
    COMPLETE,
}

val musicGenres = listOf(
    "Pop",
    "Rock",
    "Hip-Hop",
    "R&B",
    "Jazz",
    "Country",
    "Electronic",
    "Folk",
    "Classical",
    "Metal",
    "Rap",
    "K-Pop",
    "Turkish",
    "Indie",
    "Soul",
    "Reggae",
    "Latin",
    "Funk",
    "Gospel",
    "Emo",
)

val sampleArtists = listOf(
    "The Weeknd",
    "Taylor Swift",
    "Drake",
    "Ariana Grande",
    "Ed Sheeran",
    "Billie Eilish",
    "Harry Styles",
    "Dua Lipa",
    "The Beatles",
    "Queen",
    "Pink Floyd",
    "Metallica",
    "Eminem",
    "Kanye West",
    "Travis Scott",
    "Post Malone",
    "Olivia Rodrigo",
    "Bad Bunny",
    "Adele",
    "Beyoncé",
    "Jay-Z",
    "Kendrick Lamar",
    "Coldplay",
    "Imagine Dragons",
    "OneRepublic",
    "David Bowie",
    "Rolling Stones",
    "Led Zeppelin",
    "Pink",
    "Britney Spears",
    "Rihanna",
    "Usher",
    "Bruno Mars",
    "The Weeknd",
    "Weeknd",
    "Şebnem Ferah",
    "Gülşen",
    "Tarkan",
    "Sertab Erener",
    "Müslüm Gürses",
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onComplete: (OnboardingState) -> Unit,
) {
    val scrollState = rememberScrollState()
    val selectedGenders = remember { mutableStateListOf<String>() }
    val selectedGenres = remember { mutableStateListOf<String>() }
    val selectedArtists = remember { mutableStateListOf<String>() }
    var searchQuery by remember { mutableStateOf("") }
    var currentStep by remember { mutableStateOf(OnboardingStep.WELCOME) }
    val focusManager = LocalFocusManager.current

    val filteredArtists =
        if (searchQuery.isBlank()) {
            sampleArtists
        } else {
            sampleArtists.filter { it.contains(searchQuery, ignoreCase = true) }
        }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
            ) {
                // Progress Bar
                val progress = (currentStep.ordinal) / OnboardingStep.values().size.toFloat()
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    strokeCap = StrokeCap.Round,
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Content
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "onboarding",
                ) { step ->
                    when (step) {
                        OnboardingStep.WELCOME -> WelcomeStep()
                        OnboardingStep.GENDER -> GenderSelectionStep(selectedGenders)
                        OnboardingStep.GENRES ->
                            GenreSelectionStep(
                                selectedGenres,
                                musicGenres,
                            )
                        OnboardingStep.ARTISTS ->
                            ArtistSelectionStep(
                                selectedArtists,
                                filteredArtists,
                                searchQuery,
                                onSearchChange = { searchQuery = it },
                            )
                        OnboardingStep.COMPLETE -> CompleteStep()
                    }
                }
            }

            // Navigation Buttons
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (currentStep.ordinal > 0) {
                    Button(
                        onClick = {
                            currentStep = OnboardingStep.values()[currentStep.ordinal - 1]
                        },
                        modifier = Modifier.weight(1f),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                    ) {
                        Text("Geri")
                    }
                }

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        if (currentStep.ordinal < OnboardingStep.values().size - 1) {
                            currentStep = OnboardingStep.values()[currentStep.ordinal + 1]
                        } else {
                            onComplete(
                                OnboardingState(
                                    gender = selectedGenders.firstOrNull(),
                                    favoriteGenres = selectedGenres.toList(),
                                    favoriteArtists = selectedArtists.toList(),
                                ),
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled =
                        when (currentStep) {
                            OnboardingStep.WELCOME -> true
                            OnboardingStep.GENDER -> selectedGenders.isNotEmpty()
                            OnboardingStep.GENRES -> selectedGenres.size >= 3
                            OnboardingStep.ARTISTS -> selectedArtists.size >= 10
                            OnboardingStep.COMPLETE -> true
                        },
                ) {
                    Text(
                        if (currentStep == OnboardingStep.ARTISTS) "Başla"
                        else "Sonraki",
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeStep() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = null,
            modifier = Modifier.height(80.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Mualla-Music'e Hoş Geldin",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Kişiselleştirilmiş müzik deneyimi yaşamak için birkaç soruya cevap ver",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun GenderSelectionStep(selectedGenders: MutableList<String>) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Cinsiyetini Seç",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Bu, içerik önerileri için yardımcı olur",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))

        listOf("Kadın", "Erkek", "Diğer").forEach { gender ->
            GenderButton(
                text = gender,
                isSelected = gender in selectedGenders,
                onClick = {
                    selectedGenders.clear()
                    selectedGenders.add(gender)
                },
            )
        }
    }
}

@Composable
private fun GenderButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(56.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant,
                contentColor =
                    if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface,
            ),
    ) {
        Text(text = text)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenreSelectionStep(
    selectedGenres: MutableList<String>,
    genres: List<String>,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Müzik Türlerini Seç",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "En az 3 müzik türünü seç",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            genres.forEach { genre ->
                FilterChip(
                    selected = genre in selectedGenres,
                    onClick = {
                        if (genre in selectedGenres) {
                            selectedGenres.remove(genre)
                        } else {
                            selectedGenres.add(genre)
                        }
                    },
                    label = { Text(genre) },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ArtistSelectionStep(
    selectedArtists: MutableList<String>,
    filteredArtists: List<String>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Favori Sanatçılarını Seç",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "En az 10 sanatçı seç",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Sanatçı ara...") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {}),
            singleLine = true,
        )

        Text(
            text = "Seçildi: ${selectedArtists.size}/10",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(filteredArtists) { artist ->
                ArtistChip(
                    artist = artist,
                    isSelected = artist in selectedArtists,
                    onClick = {
                        if (artist in selectedArtists) {
                            selectedArtists.remove(artist)
                        } else {
                            if (selectedArtists.size < 20) {
                                selectedArtists.add(artist)
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun ArtistChip(
    artist: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    color =
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                )
                .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = artist,
            modifier = Modifier.padding(12.dp),
            color =
                if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun CompleteStep() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.favorite_filled),
            contentDescription = null,
            modifier = Modifier.height(80.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Tamamlandı!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Kişiselleştirilmiş müzik deneyimini keşfetmeye hazır mısın?",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
