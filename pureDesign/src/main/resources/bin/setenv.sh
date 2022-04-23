# Local address default as below, if you have more net-interface, you must specify one
LOCAL_ADDRESS=`/sbin/ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v inet6|awk '{print $2}'|tr -d "addr:"`
#echo LOCAL_ADDRESS:$LOCAL_ADDRESS

# Solves the problem of multiple NIC
LOCAL_ADDRESS=`echo $LOCAL_ADDRESS | awk '{print $1}'`
#echo LOCAL_ADDRESS RANDOM:$LOCAL_ADDRESS


JAVA_OPTS="$JAVA_OPTS -Xms1g"
JAVA_OPTS="$JAVA_OPTS -Xmx4g"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
JAVA_OPTS="$JAVA_OPTS -Duser.timezone=GMT+08"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote=172.31.236.105"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=3556"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
