// 
// Decompiled by Procyon v0.5.36
// 

package me.usti.banks_hold.utils;

import org.jetbrains.annotations.Nullable;
import me.usti.banks_hold.configs.DataUtils;
import java.text.DecimalFormat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Expansions extends PlaceholderExpansion
{
    public boolean  persist() {
        return true;
    }
    
    @NotNull
    public String getIdentifier() {
        return "banks";
    }
    
    @NotNull
    public String getAuthor() {
        return "destroy";
    }
    
    @NotNull
    public String getVersion() {
        return "1.0";
    }
    
    @Nullable
    public String onPlaceholderRequest(final Player player, final String params) {
        if (params.startsWith("balance")) {
            final DecimalFormat format = new DecimalFormat("#0.00");
            final String[] split = params.split("_");
            return format.format(DataUtils.getSum(player.getName(), split[1])).replace(",", ".");
        }
        return null;
    }

}
