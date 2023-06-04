package com.marufh.pathagar.setting

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/setting")
class AdminSettingController(private val settingService: SettingService) {

    @PostMapping("/generate-hash")
    fun generateHash() = settingService.generateHash()
}
