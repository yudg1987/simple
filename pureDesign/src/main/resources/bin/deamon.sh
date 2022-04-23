#!/bin/sh
# execute commond below to auto startup after startup of computer:
# 	su -c 'echo /iflytek/server/initializr/bin/deamon.sh >> /etc/rc.local'

#####################
# resolve links - $0 may be a softlink
#####################

PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
PRGDIR=`dirname "$PRG"`

#####################
# start
#####################

nohup sh "$PRGDIR"/ctl.sh deamon >/dev/null 2>&1 &
