function getDate()
{
    let Date= new Date();
    let day = Date.getDate();
    let hours = Date.getHours();

    if(hours<12)
    {
        document.writeln("Good Morning");
    }
    else if(hours>12 && hours<14)
    {
        document.writeln("Good Afternoon");
    }
    else
    {
        document.writeln("Good Evening");
    }
}