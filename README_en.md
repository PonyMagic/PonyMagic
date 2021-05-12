# [Pony Magic 3.0](https://www.curseforge.com/minecraft/mc-mods/pony-magic/files)
[![Discord](https://img.shields.io/discord/479572843114528788?color=6A7EC2&logo=discord&logoColor=ffffff&style=flat-square)](https://discord.gg/d6cX4sa)

Pony Magic is a modification for Minecraft 1.12.2 that adds the ability to use various magic. Created for use in assembly with [Mine Little Pony](https://github.com/MineLittlePony/MineLittlePony)
The mod adds four "races" of ponies, an energy system (mana) and 40+ abilities.

### Installation:
- install [Minecraft forge 1.12.2](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html)
- download [Potion Core 1.8](https://www.curseforge.com/minecraft/mc-mods/potion-core/files/2736248) and copy it to the `mods/` folder
- download [Pony Magic](https://www.curseforge.com/minecraft/mc-mods/pony-magic/files) and copy to `mods/`

### Configuration:
Most of the mod's elements can be configured using the config files in the `config/ponymagic/` folder

### Usage:
First you need to choose the race (each has unique abilities).
- Unicorns (`unicorn`) - can teleport, heal themselves and allies, swim in lava and manage item enchantments
- Earthpony (`earthpony`) - resistant in battle, can accelerate the growth of plants and repair equipment
- Zebras (`zebra`) - can control the effects of potions
- Pegasus (`pegasus`) - can fly

Then, apply a race with the command: `/magic race <playername> <racename>`
After that, the player will have access to the branches of leveling the abilities of the race (button `O`) and to quests (button`P`).

##### Quests
![Quests](img/quests.png)

In order to be able to learn any ability, you need to complete quests.
Completing quests rewards an ability points, which can be spent on learning a particular skill in the abilities menu.

##### Reset Abilities
To reset the abilities, you need the "Reset Book" item or 30 levels of vanilla experience.

##### Energy / Stamina / Mana / Blue Bar
![Stamina](img/stamina.png)

Initially, all races have 100 units of stamina.
Recovery occurs if the player don't hungry and stay on the block. Also, there is weak regeneration if the player is swimming in water.
The maximum amount and speed of regeneration can be increased using the corresponding abilities.
Stamina will be spent on using active abilities and when flying with pegasus.
There are some special skills that spend stamina on certain actions or do not require it at all.
##### Abilities / skills
![Abilities](img/unicorn.png)

To gain access to a skill, you need to study it for ability points.
Abilities are divided into two types: active and passive.
Active are used with the command `/cast <spellname>`
Passives activate themselves or act constantly.
Some abilities can be turned off (absolute shield).
A short description of the skill is in the tooltip in the abilities window.
Also, there is a racial ability that does not need to be learned.
The initial racial abilities of unicorns and earthpony depend on the server build.

#### Commands:
Race Change:
`/magic race <username> <race>`
Race Testing (Max Level):
`/magic test <race>`
Using the ability:
`/cast <skillname>`
Skill leveling:
`/magic spell <username> <spellname>`
Level change:
`/magic setlevel <username> <level>`
Changing the number of available ability points:
`/magic setpoints <username> <points>`
Checking the amount of energy:
`/stamina check [username]`
Remove all energy:
`/stamina zero | empty [username]`
Restore all energy:
`/stamina fill | restore [username]`
Add a certain amount:
`/stamina add [username] <amount>`
Change by a certain amount:
`/stamina set [username] <amount>`
Set maximum energy level:
`/stamina setmax [username] <amount>`

### Screenshots:
Unicorn Abilities:
![Unicorn Abilities](img/unicorn.png)

Earthpony Abilities:
![Earthpony Abilities](img/earthpony.png)

Pegasus Abilities:
![Pegasus Abilities](img/pegasus.png)

Zebra Abilities:
![Zebra Abilities](img/zebra.png)



### Many thanks to these ponies:
- Akell - for organizing the development; help with algorithms and abilities; drawing textures; testing
- Orhideous - for help with debugging and refactoring the code
- Polyacov_Yury - for help with debugging and refactoring the code
- Jerry - for ability ideas; drawing textures; testing
- KostaRMax - for texture drawing and testing
- Queen_Stan - for texture drawing and testing
- Zloba - for drawing textures
- Kwaarr - for ability ideas and testing
- XenoOxotnik - for ability ideas and testing
- Tol_Eres - for ability ideas and testing
- Holllow - for ability ideas and testing
- Wecanfly - for ability ideas and testing
- Walking_Trouble - for ability ideas and testing
- Jorest - for testing
- niroy - for ability ideas
- Lavenda - for ability ideas