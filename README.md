# Kanyuji.de API
This is a pretty old project and is not optimized but should give your Minecraft Server a good foundation if you're getting started

# SQL Tables

Read the <a href="https://github.com/brentspine/Kanyuji-DE-Lobby">Lobby README.md</a> file for information, like features and setup

<h2>playersIgnored</h2>

This table stores the data for the `/ignore` command and is handled by <a href="/src/main/java/de/brentspine/kanyujiapi/mysql/data/MySQLChat.java">MySQLChat</a>

| Field | Type | Extra |
|-------|-------| ----- |
| uuid | varchar(36) | |
| ignored | varchar(36) ||


<h2>coins</h2>

This table stores the data for the coin system and is handled by <a href="/src/main/java/de/brentspine/kanyujiapi/mysql/data/MySQLCoins.java">MySQLCoins</a>

| Field | Type | Extra |
|-------|-------| ----- |
| UUID | varchar(36) | |
| Coins | int | |


<h2>friends</h2>

This table stores the data for the `/friends` command and is handled by <a href="/src/main/java/de/brentspine/kanyujiapi/mysql/data/MySQLFriends.java">MySQLFriends</a>

| Field | Type | Extra |
|-------|-------| ----- |
| user1 | varchar(36) | |
| user2 | varchar(36) | |


<h2>chestKeys</h2>

This table stores the data for the `/keys` command and the chest system and is handled by <a href="/src/main/java/de/brentspine/kanyujiapi/mysql/data/MySQLLootChests.java">MySQLLootChests</a>

| Field | Type | Extra |
|-------|-------| ----- |
| UUID | varchar(36) | |
| scratchOff | int | |
| dices | int | |
| slots | int | |
| universal | int | |


<h2>messagelater</h2>

This table stores the data for the `/messagelater` command and is handled by <a href="/src/main/java/de/brentspine/kanyujiapi/mysql/data/MySQLMessageLater.java">MySQLMessageLater</a><br>
Not sure about the type of message, might get an error. If so change it and edit this file

| Field | Type | Extra |
|-------|-------| ----- |
| target | varchar(36) | |
| sender | varchar(36) | |
| message | varchar(240) | |



<h2>playtime</h2>

This table stores the data for the `/playtime` command and is handled by <a href="/src/main/java/de/brentspine/kanyujiapi/mysql/data/MySQLPlaytime.java">MySQLPlaytime</a>

| Field | Type | Extra |
|-------|-------| ----- |
| UUID | varchar(36) | |
| minutesPlayed | int | |


<h2>SurfStats</h2>

This tabale stores the data for the gamemode Surf. It is not part of the Lobby System but still needs to be included probably to ensure that the plugin works. You can remove it if you want. Handled by href="/src/main/java/de/brentspine/kanyujiapi/mysql/data/MySQLSurf.java">MySQLSurf</a><br>
Also having the points field doesn't make sense, but yeaaaaaa... This was my first SQL experience and everything.

| Field | Type | Extra |
|-------|-------| ----- |
| uniqueID | varchar(36) | |
| kills | int | |
| deaths | int | |
| points | int | |


