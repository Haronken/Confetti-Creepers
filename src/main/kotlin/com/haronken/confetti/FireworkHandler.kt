package com.haronken.confetti

import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class FireworkHandler {

    /**
     * Rastgele bir havai fişek efekti oluşturur.
     */
    fun getRandomFireworkEffect(): FireworkEffect {
        return FireworkEffect.builder()
            .with(FireworkEffect.Type.entries.random()) // Rastgele tip
            .withColor(randomColors())                 // Rastgele renkler
            .withFade(randomColors())                  // Rastgele solma renkleri
            .flicker(Random.nextBoolean())             // Rastgele yanıp sönme
            .trail(Random.nextBoolean())               // Rastgele iz bırakma
            .build()
    }

    /**
     * Hangi durumda olursa olsun Creeper'ı öldürdüğünde ya da patladığında havai fişek patlat.
     */
    fun handleFireworkExplosion(entity: Entity) {
        val world = entity.world

        // Creeper'ın gövdesine biraz yukarıda patlamayı yap
        val explosionLocation = entity.location.add(0.0, 0.5, 0.0)

        // Havai fişek patlat
        val firework = world.spawn(explosionLocation, org.bukkit.entity.Firework::class.java)
        val fireworkMeta = firework.fireworkMeta
        fireworkMeta.addEffects(getRandomFireworkEffect())
        firework.fireworkMeta = fireworkMeta
        firework.detonate() // Anında patlat
    }

    /**
     * Rastgele 2-3 renk seçer.
     */
    private fun randomColors(): List<Color> {
        return List(Random.nextInt(1, 3)) {
            Color.fromRGB(
                Random.nextInt(256), // 0-255 arası kırmızı
                Random.nextInt(256), // 0-255 arası yeşil
                Random.nextInt(256)  // 0-255 arası mavi
            )
        }
    }

    /**
     * Rastgele 2-4 boya eşyası döndürür, her biri 1-2 adet olacak şekilde.
     */
    fun getRandomFireworkItems(): List<ItemStack> {
        val dyeMaterials = listOf(
            Material.RED_DYE,
            Material.BLUE_DYE,
            Material.GREEN_DYE,
            Material.YELLOW_DYE,
            Material.PURPLE_DYE,
            Material.ORANGE_DYE,
            Material.LIGHT_BLUE_DYE, // Aqua DYE yerine Light Blue DYE
            Material.MAGENTA_DYE,
            Material.PINK_DYE,
            Material.LIME_DYE,
            Material.CYAN_DYE,
            Material.BROWN_DYE,
            Material.GRAY_DYE,
            Material.LIGHT_GRAY_DYE,
            Material.WHITE_DYE,
            Material.BLACK_DYE
        )

        val fireworkMaterials = listOf(
            Material.GUNPOWDER, // Barut
            Material.PAPER       // Kağıt
        )

        // Boya materyallerinden rastgele 3 tanesini seç
        val selectedDyeMaterials = dyeMaterials.shuffled().take(3)

        // Seçilen boyaları ve diğer malzemeleri birleştir
        val allMaterials = selectedDyeMaterials + fireworkMaterials

        // Listeyi rastgele bir uzunlukta seç
        val numItemsToReturn = Random.nextInt(2, 6) // 2 ile 5 arasında rastgele sayı

        // Listeyi karıştır ve belirtilen sayıda öğe al
        val finalMaterials = allMaterials.shuffled().take(numItemsToReturn)

        // ItemStack'lerin miktarı 1-2 arasında rastgele belirlenir
        return finalMaterials.map { material ->
            ItemStack(material, Random.nextInt(1, 3)) // 1 ile 2 arasında rastgele adet
        }
    }
}
