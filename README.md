
Overview
=======

This is a Neoforged Minecraft mod intended for use with the Echo Sculk Dragon (https://www.curseforge.com/minecraft/mc-mods/echo-sculk-dragon-for-dragon-survival)

Despite this, you can use it anywhere you want, too.  It adds a number of MobEffects that you can add with /effect or other methods.

List of MobEffects:

sculkdragonutils:sculk_harmony - This suppresses vibrations of whatever the potency is, and below. In version 1.1, it also prevents sculk sensors from activating when you walk on them.

sculkdragonutils:warden_harmony - This prevents the Warden from targeting whatever has it

sculkdragonutils:sculk_sight - This adds the Sobel shader from the 1.9 SuperSecretSettings

sculkdragonutils:reverberating - This is a dummy effect that does nothing, but Sculk Shriekers will apply it to nearby mobs.
You can use this as a trigger or check for other things.

sculkdragonutils:sculk_bloom (v1.1 only) - This makes kills worth below 50 exp (To exclude wither, ender dragon, and modded bosses) to spawn a sculk spreader if it's able to.  Potency acts as a multiplier, with 10% added effect per level.

List of Commands (v1.1 only):

/sculk-bloom <x> <y> <z> <amount> <worldgen> - This is a command that triggers a sculk bloom (like from the sculk catalyst) at the specified location. Worldgen makes it spread farther and the generated shriekers can spawn the Warden.

Credits:
============
Code for the Sobel shader implementation is from the Goggles mod (https://modrinth.com/mod/goggles) by IceWasTaken

Also because the code is the same, it's unfortunately not compatible...  I will potentially look into compatibility later.

Mapping Names:
============
By default, the MDK is configured to use the official mapping names from Mojang for methods and fields 
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/NeoForged/NeoForm/blob/main/Mojang.md

Additional Resources: 
==========
Community Documentation: https://docs.neoforged.net/  
NeoForged Discord: https://discord.neoforged.net/
