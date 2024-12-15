package com.haronken.confetti

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val fireworkHandler = FireworkHandler() // Firework işlemleri için handler oluştur
        val eventListener = EventListener(fireworkHandler) // Event listener'ı oluştur

        // Event listener'ı kaydet
        server.pluginManager.registerEvents(eventListener, this)
        logger.info("Confetti Creepers plugin is enabled!")
    }

    override fun onDisable() {
        logger.info("Confetti Creepers plugin is disabled!")
    }
}
