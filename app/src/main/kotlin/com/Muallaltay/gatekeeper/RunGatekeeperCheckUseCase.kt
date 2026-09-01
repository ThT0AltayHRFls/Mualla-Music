/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.gatekeeper

import javax.inject.Inject

class RunGatekeeperCheckUseCase
    @Inject
    constructor(
        private val repository: GatekeeperRepository,
    ) {
        suspend operator fun invoke(): GatekeeperResult = repository.checkAccess()
    }
