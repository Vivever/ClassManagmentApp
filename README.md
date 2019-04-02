# ClassManagmentApp

##Major Detected Bugs-
1. Two Attendance can't be taken in single day-(prevent two attendance in single day OR add feature to take two attendance in same day)
2. Student MainActivity RecyclerView get AutoUpdated With Duplicate Data when there is change in data in database.
3. Sometime app don't load data on appstart. Need to be manually restarted.
4. Random Crashes here and there(less frequent)
5. If Faculty endsession then all the student connected to that subject will face crashes, add a failsafe that if student can't find a class in classes tree then don't crash but handle it by showing class not found.

##Feature need to be added-
1. Common storage system to share resources.
2. Broadcast System
