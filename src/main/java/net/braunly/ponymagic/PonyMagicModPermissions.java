package net.braunly.ponymagic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class PonyMagicModPermissions 
{
	public static final Permission UNICORN = new Permission("staminamod.unicorn");
	public static final Permission EARTHPONY = new Permission("staminamod.earthpony");
	public static final Permission ZEBRA = new Permission("staminamod.zebra");
	public static final Permission PEGAS = new Permission("staminamod.pegas");
    
    
    public static PonyMagicModPermissions Instance;
    private Class<?> bukkit;
    private Method getPlayer;
    private Method hasPermission;

    public PonyMagicModPermissions() {
        Instance = this;
        try {
            this.bukkit = Class.forName("org.bukkit.Bukkit");
            this.getPlayer = this.bukkit.getMethod("getPlayer", String.class);
            this.hasPermission = Class.forName("org.bukkit.entity.Player").getMethod("hasPermission", String.class);
        }
        catch (ClassNotFoundException e) {
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPermission(EntityPlayer player, Permission permission) {
        if (PonyMagicModPermissions.Instance.bukkit != null) {
            return Instance.bukkitPermission(player.getCommandSenderName(), permission.name);
        }
        return false;
    }
    
    private boolean bukkitPermission(String username, String permission) {
        try {
            Object player = this.getPlayer.invoke(null, username);
            return (Boolean)this.hasPermission.invoke(player, permission);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasPermissionString(EntityPlayer player, String permission) {
        if (PonyMagicModPermissions.Instance.bukkit != null) {
            return Instance.bukkitPermission(player.getCommandSenderName(), permission);
        }
        return true;
    }

    public static class Permission {
        private static final List<String> permissions = new ArrayList<String>();
        public String name;

        public Permission(String name) {
            this.name = name;
            if (!permissions.contains(name)) {
                permissions.add(name);
            }
        }
    }

}

