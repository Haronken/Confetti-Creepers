package com.haronken.confetti

import org.bukkit.entity.Creeper
import org.bukkit.entity.Firework
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityExplodeEvent

class EventListener(private val fireworkHandler: FireworkHandler) : Listener {

    /**
     * Creeper öldüğünde özel efekt ve eşya düşürme işlemleri.
     */
    @EventHandler
    fun onCreeperDeath(event: EntityDeathEvent) {
        val entity = event.entity
        if (entity is Creeper) {
            fireworkHandler.handleFireworkExplosion(entity)  // Patlama için işlemi çağır

            event.drops.clear() // Varsayılan düşenleri temizle
            event.drops.addAll(fireworkHandler.getRandomFireworkItems()) // Rastgele eşya düşür
        }
    }

    /**
     * Creeper patlaması gerçekleştiğinde havai fişek patlat.
     */
    @EventHandler
    fun onCreeperExplosion(event: EntityExplodeEvent) {
        val entity = event.entity
        if (entity is Creeper) {
            event.isCancelled = true

            fireworkHandler.handleFireworkExplosion(entity)  // Patlama için işlemi çağır

            // Patlama lokasyonunda rastgele firework malzemeleri düşür
            val explosionLocation = event.location
            val items = fireworkHandler.getRandomFireworkItems()

            for (item in items) {
                explosionLocation.world?.dropItem(explosionLocation, item) // Malzeme düşür
            }
        }
    }

    /**
     * Havai fişek patlamasından dolayı oyuncuya gelen hasarı engelleyen event handler.
     *
     * Neden creeper diye sormayın, muhtemelen metodun çalışma mantığıyla alakalı
     * En yakın entitylere bakıyor ve havai fişek creeperın içinde olduğu için
     * bazen hasar creeperdan bazen de fireworkten geliyor gibi görünüyor
     * Eklenti creeperların da hasarını engellediği için sorun yok
     */
    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val damager = event.damager

        // Eğer entity bir oyuncu ve hasar veren entity bir Creeper veya Firework ise
        if (entity is Player && (damager is Creeper || damager is Firework)) {
            // Patlama Firework tarafından yapılmışsa, hasarı engelle
            event.isCancelled = true
        }

        if (entity is Item){
            // Patlamaların eşyalara zarar vermesini önle
            event.isCancelled = true
        }
    }

}
