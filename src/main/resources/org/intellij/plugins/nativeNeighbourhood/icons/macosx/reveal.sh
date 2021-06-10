#!/bin/sh
#
# Reveal a file in the Finder.
# 
# The script takes one argument, the qualified name of a file.
#
# Note that the script is necessary because osascript before osx 10.4 could not
# pass arguments.
#

/usr/bin/osascript <<EOF
	tell application "Finder"
		reveal (POSIX file "$1") as alias
		activate
	end tell
EOF
