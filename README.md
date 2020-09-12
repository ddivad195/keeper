# Keeper
A bot to allow users to save useful messages to a DM by reacting to them.

When the bot is enabled, reacting to a message with the specified reaction will save the message to that user's DM.

![Saved Message](/.github/assets/saved.png)

All available commands can be seen [here](./commands.md).

## Usage
There is a docker container supplied with this. 

1. Pull down the project using git clone.
2. Chmod +x on the runscript, so that it is executable.
3. Run the run script the with bot token, and a file path to where you want.
   the bot to output the configuration file
4. Fill in the configuration file.
5. Re-run the run command.

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
