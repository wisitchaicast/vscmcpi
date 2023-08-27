package org.doggocraft;

import org.bukkit.entity.EnderPearl;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Trident;
import org.bukkit.entity.Drowned;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("BRUH");
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler
    public void onEntitySpawn(final EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.PRIMED_TNT) {
            final TNTPrimed tnt = (TNTPrimed)event.getEntity();
            tnt.setFuseTicks(2);
        }
    }
    
    @EventHandler
    public void onDamage2(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            System.out.println(invokedynamic(makeConcatWithConstants:(Lorg/bukkit/event/entity/EntityDamageEvent;I)Ljava/lang/String;, event, ((Player)event.getEntity()).getNoDamageTicks()));
            ((Player)event.getEntity()).setMaximumNoDamageTicks(1);
            System.out.println(invokedynamic(makeConcatWithConstants:(Lorg/bukkit/event/entity/EntityDamageEvent;I)Ljava/lang/String;, event, ((Player)event.getEntity()).getNoDamageTicks()));
        }
    }
    
    @EventHandler
    public void onBlockSpread(final BlockSpreadEvent event) {
        final Block block = event.getBlock();
        if (block.getType() == Material.FIRE) {
            event.setCancelled(true);
            block.setType(Material.AIR);
            final Block newBlock = event.getSource();
            final int dx = newBlock.getX() - block.getX();
            final int dy = newBlock.getY() - block.getY();
            final int dz = newBlock.getZ() - block.getZ();
            block.getRelative(dx, dy, dz).setType(Material.FIRE);
        }
    }
    
    @EventHandler
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.SHEEP && event.getBlock().getType() == Material.GRASS_BLOCK) {
            final Sheep sheep = (Sheep)event.getEntity();
            sheep.setAI(false);
            event.getBlock().setType(Material.DIRT);
            event.getBlock().getRelative(0, 1, 0).setType(Material.GRASS_BLOCK);
        }
    }
    
    @EventHandler
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Zombie) {
            final Zombie zombie = (Zombie)event.getEntity();
        }
        if (event.getEntityType() == EntityType.SKELETON) {
            final Skeleton skeleton = (Skeleton)event.getEntity();
            this.makeSkeletonShootRapidly(skeleton);
        }
        if (event.getEntityType() == EntityType.GHAST) {
            final Ghast ghast = (Ghast)event.getEntity();
            this.makeGhastShootRapidly(ghast);
        }
        if (event.getEntityType() == EntityType.BLAZE) {
            final Blaze blze = (Blaze)event.getEntity();
            this.makeBlzeShootRapidly(blze);
        }
        if (event.getEntityType() == EntityType.DROWNED) {
            final Drowned drowned = (Drowned)event.getEntity();
            this.makeDrownedThrowRapidly(drowned);
        }
    }
    
   @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (item.getType() == Material.BOW) {
            player.launchProjectile(Arrow.class);
        } else if (item.getType() == Material.TRIDENT) {
            handleTridentInteract(event, player);
        } else if (item.getType() == Material.ENDER_PEARL) {
            handleEnderPearlInteract(event, player);
        } else if (item.getType() == Material.CROSSBOW) {
            handleCrossbowInteract(event, player, item);
        }
    }

    private void handleTridentInteract(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
        player.updateInventory();
        player.launchProjectile(Trident.class);
    }

    private void handleEnderPearlInteract(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
        player.updateInventory();
        player.launchProjectile(EnderPearl.class);
    }

    private void handleCrossbowInteract(PlayerInteractEvent event, Player player, ItemStack item) {
        event.setCancelled(true);
        player.updateInventory();

        if (!item.containsEnchantment(Enchantment.QUICK_CHARGE)) {
            item.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 1);
            player.playSound(player.getLocation(), Sound.ITEM_CROSSBOW_LOADING_START, 1.0f, 1.0f);
        } else {
            player.launchProjectile(Arrow.class);
            item.removeEnchantment(Enchantment.QUICK_CHARGE);
            player.playSound(player.getLocation(), Sound.ITEM_CROSSBOW_SHOOT, 1.0f, 1.0f);
        }
    }
    
    private void makeSkeletonShootRapidly(final Skeleton skeleton) {
        BukkitRunnable task = new BukkitRunnable() {
            public void run() {
                if (skeleton.isDead()) {
                    this.cancel();
                    return;
                }
                if (skeleton.getTarget() != null) {
                    skeleton.launchProjectile(Arrow.class);
                }
            }
        };
        task.runTaskTimer(this, 0L, 1L);
    }
    
    private void makeBlzeShootRapidly(final Blaze blaze) {
        new BukkitRunnable() {
            public void run() {
                if (blaze.isDead()) {
                    this.cancel();
                    return;
                }
                if (blaze.getTarget() != null) {
                    final double gx = blaze.getLocation().getX();
                    final double gy = blaze.getLocation().getY();
                    final double gz = blaze.getLocation().getZ();
                    final double tx = blaze.getTarget().getLocation().getX();
                    final double ty = blaze.getTarget().getLocation().getY();
                    final double tz = blaze.getTarget().getLocation().getZ();
                    final double dist = Math.sqrt(Math.pow(gx - tx, 2.0) + Math.pow(gy - ty, 2.0) + Math.pow(gz - tz, 2.0));
                    final Fireball fireball = (Fireball)blaze.launchProjectile((Class)Fireball.class);
                    fireball.teleport(new Location(blaze.getWorld(), gx + (tx - gx) / dist, gy + (ty - gy) / dist, gz + (tz - gz) / dist));
                    fireball.setVelocity(new Vector((tx - gx) / dist, (ty - gy) / dist, (tz - gz) / dist));
                }
            }
        }.runTaskTimer((Plugin)this, 0L, 1L);
    }
    
    private void makeDrownedThrowRapidly(final Drowned drowned) {
        new BukkitRunnable() {
            public void run() {
                if (drowned.isDead()) {
                    this.cancel();
                    return;
                }
                if (drowned.getTarget() != null) {
                    drowned.launchProjectile((Class)Trident.class);
                }
            }
        }.runTaskTimer((Plugin)this, 0L, 1L);
    }

    @EventHandler
    public void onPlayerInteract2(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getType().equals((Object)Material.BOW)) {
            player.launchProjectile((Class)Arrow.class);
        }    
        if (player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
            event.setCancelled(true);
            player.updateInventory();
            if (player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
                player.launchProjectile((Class)Trident.class);  
            }    
        }
        if (player.getInventory().getItemInMainHand().getType() == Material.ENDER_PEARL) {
            event.setCancelled(true);
            player.updateInventory();
            if (player.getInventory().getItemInMainHand().getType() == Material.ENDER_PEARL) {
                player.launchProjectile((Class)EnderPearl.class);
            }
        }
    }
}
