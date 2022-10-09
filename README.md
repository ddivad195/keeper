# Keeper
A bot to allow users to save useful messages to a DM by reacting to them. Also allows for "Slack like" reminders to be created via the Discord "Apps" menu accessed by right clicking on a message.

When the bot is enabled, reacting to a message with the specified reaction will save the message to that user's DM.

![Saved Message](/.github/assets/saved.png)

All available commands can be seen [here](./commands.md).

## Usage
Since this bot has a docker-compose file, all you need to start your own Keeper instance is:

```
$ cp .env.example .env
```
Edit the .env file with your favourite editor, filling out the token and default prefix
```
$ docker-compose up --detach
```

## Configuration
To configure the bot for use on a Discord server, the following process should be used:

The default prefix is "keeper!", or tagging the bot can be used as well.

`keeper!configure`

![Configuration](/.github/assets/configuration.png)

## License

```
MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
