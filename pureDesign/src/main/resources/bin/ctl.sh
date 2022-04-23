#!/bin/sh

# resolve links - $0 may be a softlink
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
CONSOLE_OUT=$PRGDIR/../logs/console.out

# Only set PRG_HOME if not already set
[ -z "$PRG_HOME" ] && PRG_HOME=`cd "$PRGDIR/.." ; pwd`

#echo "PRG_HOME: $PRG_HOME"

if [ -r "$PRG_HOME"/bin/setenv.sh ]; then
  . "$PRG_HOME"/bin/setenv.sh
fi

# Some parameters initializaion
[ -z "$FILE_ENCODING" ] && FILE_ENCODING=UTF-8
[ -z "$TMP_DIR" ] && TMP_DIR="$PRG_HOME"/tmp
[ -z "$DATA_DIR" ] && DATA_DIR="$PRG_HOME"/data
[ -z "$RUN_DIR" ] && RUN_DIR="$PRG_HOME"/run
[ -z "$LOG_DIR" ] && LOG_DIR="$PRG_HOME"/logs
[ -z "$CONF_DIR" ] && CONF_DIR="$PRG_HOME"/conf/

if [ ! -d "$TMP_DIR" ]; then
mkdir -p $TMP_DIR
fi

PRG_NAME=@project.artifactId@
PRG_VERSION=@project.version@
JAR_FILE=$PRG_HOME/lib/$PRG_NAME-$PRG_VERSION.jar

PRG_PIDFILE=$RUN_DIR/$PRG_NAME.pid
LIB_PATH="$PRG_HOME/lib"
CONF_PATH=$CONF_DIR

SPRING_CONFIGS="file:$CONF_PATH"

#JAVA_OPTS="$JAVA_OPTS -Djava.ext.dirs=$LIB_PATH"
JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=$FILE_ENCODING"
JAVA_OPTS="$JAVA_OPTS -Djava.tmp.dir=$TMP_DIR"
JAVA_OPTS="$JAVA_OPTS -Djava.io.tmpdir=$TMP_DIR"

JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"
JAVA_OPTS="$JAVA_OPTS -Dspring.config.location=$SPRING_CONFIGS"

JAVA_OPTS="$JAVA_OPTS -Diflytek.dir.log=$LOG_DIR"
JAVA_OPTS="$JAVA_OPTS -Diflytek.dir.data=$DATA_DIR"
JAVA_OPTS="$JAVA_OPTS -Diflytek.dir.run=$RUN_DIR"
JAVA_OPTS="$JAVA_OPTS -Diflytek.dir.conf=$CONF_PATH"
JAVA_OPTS="$JAVA_OPTS -Diflytek.file.run=$PRG_PIDFILE"

PRG_START="nohup java -Xbootclasspath/a:$CONF_PATH -server $JAVA_OPTS -jar $JAR_FILE"

PRG_STATUS=""
PID=""
ERROR=0

get_pid() {
    PID=""
    PIDFILE=$1
    # check for pidfile
    if [ -f "$PIDFILE" ] ; then
        PID=`cat $PIDFILE`
    fi
}

get_PRG_PID() {
    get_pid $PRG_PIDFILE
    if [ ! "$PID" ]; then
        return
    fi
    if [ "$PID" -gt 0 ]; then
        PRG_PID=$PID
    fi
}

is_service_running() {
    PID=$1
    if [ "x$PID" != "x" ] && kill -0 $PID 2>/dev/null ; then
        RUNNING=1
    else
        RUNNING=0
    fi
    return $RUNNING
}

is_running() {
    get_PRG_PID
    is_service_running $PRG_PID
    RUNNING=$?
    if [ $RUNNING -eq 0 ]; then
        PRG_STATUS="$PRG_NAME not running"
    else
        PRG_STATUS="$PRG_NAME already running"
    fi
    return $RUNNING
}

start() {
    is_running
    RUNNING=$?
    if [ $RUNNING -eq 1 ]; then
        echo "$0 $ARG: $PRG_NAME  (pid $PRG_PID) already running"
	exit
    fi
	echo $PRG_START
    $PRG_START > /dev/null 2>&1 &
    echo $! > $PRG_PIDFILE
    COUNTER=40
    while [ $RUNNING -eq 0 ] && [ $COUNTER -ne 0 ]; do
        COUNTER=`expr $COUNTER - 1`
        sleep 3
        is_running
        RUNNING=$?
    done
    if [ $RUNNING -eq 0 ]; then
        ERROR=1
    fi
    if [ $ERROR -eq 0 ]; then
	    echo "==========$0 $ARG: $PRG_NAME  started at port $PORT=========="
	    sleep 2
    else
	    echo "==========$0 $ARG: $PRG_NAME  could not be started=========="
	    ERROR=3
    fi
}

stop() {
    NO_EXIT_ON_ERROR=$1
    is_running
    RUNNING=$?
    if [ $RUNNING -eq 0 ]; then
        echo "$0 $ARG: $PRG_STATUS"
        if [ "x$NO_EXIT_ON_ERROR" != "xno_exit" ]; then
            exit
        else
            return
        fi
    fi

	kill $PRG_PID

    COUNTER=40
    while [ $RUNNING -eq 1 ] && [ $COUNTER -ne 0 ]; do
        COUNTER=`expr $COUNTER - 1`
        sleep 3
        is_running
        RUNNING=$?
    done

    is_running
    RUNNING=$?
    if [ $RUNNING -eq 0 ]; then
            echo "==========$0 $ARG: $PRG_NAME stopped=========="
            rm -f $PRG_PIDFILE
        else
            echo "==========$0 $ARG: $PRG_NAME could not be stopped=========="
            ERROR=4
    fi
}

help(){
    echo    "--------------------------------------------------"
    echo    "Usage: ctl.sh  help|start|stop|status|cleanpid|deamon|version"
    echo -e "\n"
    echo    "start      - start the process"
    echo    "stop       - stop the process"
    echo    "status     - query the process status"
    echo    "cleanpid   - delete the process pid file"
    echo    "cleanlogs  - delete all log files"
    echo    "cleantmp   - delete all temp files"
    echo    "clean      - delete all temp/log/pid files"
    echo    "deamon     - set the process as a deamon"
    echo    "version    - view build version"
    echo    "help       - print help"
}

cleanlogs() {
    rm -rf $LOG_DIR/*
}

cleantmp() {
    rm -rf $TMP_DIR/*
}

cleanpid() {
    rm -f $PRG_PIDFILE
}

clean() {
    cleanpid
    cleantmp
    cleanlogs
}

version() {
    jar -xf $JAR_FILE META-INF/build-info.properties
    cat META-INF/build-info.properties
    rm -rf META-INF
}

if [ "x$1" = "xstart" ]; then
    start
elif [ "x$1" = "xstop" ]; then
    stop
elif [ "x$1" = "xstatus" ]; then
    is_running
    echo "$PRG_STATUS"
elif [ "x$1" = "xcleanpid" ]; then
    cleanpid
elif [ "x$1" = "xcleantmp" ]; then
    cleantmp
elif [ "x$1" = "xcleanlogs" ]; then
    cleanlogs
elif [ "x$1" = "xclean" ]; then
    clean
elif [ "x$1" = "xversion" ]; then
    version
elif [ "x$1" = "xdeamon" ]; then
    is_running
    RUNNING=$?
    if [ $RUNNING -eq 1 ]; then
        echo "$0 $ARG: $PRG_NAME  (pid $PRG_PID) already running"
    else
        echo "$0 $ARG: $PRG_NAME  (pid $PRG_PID) not running"
        start
    fi
elif [ "x$1" = "xhelp" ]; then
    help
else
	help
fi

exit $ERROR
