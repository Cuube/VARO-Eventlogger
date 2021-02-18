# VARO-Eventlogger
A Minecraft Plugin made by request for Spigot and related forks that logs player connects & disconnects into your discord guild.

## Example Configuration File
```json
{
  "botToken": "YOUR DISCORD BOT TOKEN HERE",
  "channels": {
    "main": 811659522661023744,
    "coordinates": 811659789679198249
  },
  "messages": {
    "logon": "🡆 %p joined",
    "logoff": "🡄 %p left",
    "death": "⛌ %p died",
    "worldName": "World Name",
    "worldType": "World Type"
  }
}
```
`botToken` - required, so that the plugin knows to which bot to try to connect to

`channels.main` - main channel in which player joins and quits are logged in

`channels.coordinates` - a channel which only logs disconnects, but with a more detailed description than it does in `channels.main`. This includes the coordinates, world and the health the player had before disconnecting. Intended for staff members

`messages.[...]` - should be obvious. customization!
