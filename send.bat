@echo off
git add .
git commit -m $1
git push heroku master
heroku open
pause