#!/bin/sh
#
# Reveal a directory in the Terminal.
# 
# The script takes one argument, the qualified name of a directory.
#
# Note that the script is necessary because osascript before osx 10.4 could not
# pass arguments.
#

/usr/bin/osascript <<EOF
	tell application "Terminal"
		do script with command "cd " & quote & "$1" & quote
		activate
	end tell
EOF
