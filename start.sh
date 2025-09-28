#!/bin/sh
set -eu

export DISPLAY=:99
SCREEN_GEOMETRY="1280x800x24"

Xvfb ${DISPLAY} -screen 0 ${SCREEN_GEOMETRY} -nolisten tcp &
XVFB_PID=$!

sleep 1

x11vnc -display ${DISPLAY} -forever -shared -nopw -rfbport 5900 -listen 0.0.0.0 \
  -xkb -noxrecord -noxfixes -noxdamage &
X11VNC_PID=$!

java -cp "Biblioteca.jar:lib/mysql-connector-j-9.4.0.jar" application.Main &
APP_PID=$!

NOVNC_UTIL="/usr/share/novnc/utils/novnc_proxy"
if [ -x "$NOVNC_UTIL" ]; then
  echo "noVNC disponível em http://0.0.0.0:6080"
  exec "$NOVNC_UTIL" --vnc localhost:5900 --listen 0.0.0.0:6080
else
  echo "Usando websockify diretamente (noVNC): http://0.0.0.0:6080"
  WEB_DIR="/usr/share/novnc"
  if [ ! -d "$WEB_DIR" ]; then
    echo "Aviso: diretório noVNC não encontrado em $WEB_DIR. A UI web pode não carregar corretamente."
  fi
  exec websockify --web "$WEB_DIR" 0.0.0.0:6080 localhost:5900
fi

kill $APP_PID $X11VNC_PID $XVFB_PID 2>/dev/null || true
wait