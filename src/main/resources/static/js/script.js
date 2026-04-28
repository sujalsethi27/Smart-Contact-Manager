console.log("Hello, world!");

var togglesidebar = () => {
    if($(".sidebar").is(":visible")) {
        //true
        //band krna hai sidebar kyuki vo visible hai

        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%")

    }else {
        //false
        //show karna hai
          $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%")
    }
}

