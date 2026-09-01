/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.Muallaltay.R
import com.Muallaltay.ads.presentation.SupportMualla-MusicScreenState
import com.Muallaltay.ads.presentation.SupportMualla-MusicUiEvent
import com.Muallaltay.ads.presentation.SupportMualla-MusicViewModel

internal const val supportMualla-MusicAvailable = true

@Composable
internal fun SupportMualla-MusicSection(
    modifier: Modifier = Modifier,
    onMessage: (String) -> Unit,
    viewModel: SupportMualla-MusicViewModel = hiltViewModel(),
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val failureMessage = stringResource(R.string.support_mualla_failed)
    val openSupportPage = remember(viewModel) { viewModel::onSupportMualla-MusicClick }

    LaunchedEffect(viewModel, onMessage, failureMessage) {
        viewModel.events.collect { event ->
            when (event) {
                SupportMualla-MusicUiEvent.OpenFailed -> onMessage(failureMessage)
            }
        }
    }

    SupportMualla-MusicCard(
        state = state,
        onClick = openSupportPage,
        modifier = modifier,
    )
}

@Composable
private fun SupportMualla-MusicCard(
    state: SupportMualla-MusicScreenState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    val iconContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    val iconContainerModifier =
        remember(iconContainerColor) {
            Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(iconContainerColor)
        }
    val description =
        when (state) {
            is SupportMualla-MusicScreenState.Loading -> {
                stringResource(R.string.support_mualla_preparing)
            }

            is SupportMualla-MusicScreenState.Success -> {
                stringResource(R.string.support_mualla_description)
            }

            is SupportMualla-MusicScreenState.Empty -> {
                stringResource(R.string.support_mualla_unavailable)
            }

            is SupportMualla-MusicScreenState.Error -> {
                stringResource(R.string.support_mualla_retry)
            }
        }

    Card(
        onClick = onClick,
        enabled =
            state !is SupportMualla-MusicScreenState.Loading &&
                state !is SupportMualla-MusicScreenState.Empty,
        shape = MaterialTheme.shapes.extraLarge,
        colors =
            CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = containerColor,
                disabledContentColor = contentColor.copy(alpha = 0.7f),
            ),
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = 128.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 22.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = iconContainerModifier,
            ) {
                Icon(
                    painter = painterResource(R.drawable.star),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(34.dp),
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.support_mualla_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(64.dp),
            ) {
                if (state is SupportMualla-MusicScreenState.Loading) {
                    CircularProgressIndicator(
                        color = contentColor,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(28.dp),
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.play),
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(32.dp),
                    )
                }
            }
        }
    }
}
