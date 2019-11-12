#!/bin/sh
SERVICE_NAME=SrvFcmServer


#for filename in ./lib/*.jar
#do
#    CLASSPATH=${CLASSPATH}:$filename
#done

#echo "${CLASSPATH}"

PATH_TO_JAR=./SrvFcmServer.jar
PID_PATH_NAME=./SrvFcServer.pid
JAVA_OPT="-Xms2048m -Xmx2048m"
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            nohup java -DServerPrivix=B -Dlog_home=/home/srv -Dspring.profiles.active=real -jar $JAVA_OPT $PATH_TO_JAR >> ./out.log &
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stoping ..."
            kill $PID;
            echo "$SERVICE_NAME stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ...";
            kill $PID;
            echo "$SERVICE_NAME stopped ...";
            rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting ..."
            nohup java -jar $JAVA_OPT $PATH_TO_JAR >> /dev/null &
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac