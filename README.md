This project aim to enable RTSP on EzViz camera from command line.

The issue
---
EzViz delivered some time ago a new firmware where RTSP is disabled by default.

It can be re-enabled using the app :
`EZVIZ IOS app–>Profile–>Settings–>Lan Live View–>(Start scanning)–>Select Camera–>Login–>Settings–>Local Service Settings–>Enable RTSP`

But at each camera reboot (at least on my C3X), the RTSP is disabled again.

The solution
---
This docker image use the hikvision native SDK to enable RTSP on a camera.
With a cron or a failure detection mechanism, it allow automatic re-enablement of RTSP.

Usage
---
`docker run --rm ezviz-enable-rtsp --host=192.168.0.10 --port=8000 --username=admin --password=foobar`
