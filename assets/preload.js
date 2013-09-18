$(document).ready(function () {
	"use strict"
	//indexOf is not supported by IE9>. 
	if (!Array.prototype.indexOf){
	  Array.prototype.indexOf = function(elt /*, from*/){
	    var len = this.length >>> 0;

	    var from = Number(arguments[1]) || 0;
	    from = (from < 0)
	         ? Math.ceil(from)
	         : Math.floor(from);
	    if (from < 0)
	      from += len;

	    for (; from < len; from++){
	      if (from in this &&
	          this[from] === elt)
	        return from;
	    }
	    return -1;
	  };
	}
    
    var bgImg = [], img = [], count=0, percentage = 0;

    //Searching all elemnts in the page for image
    $('*').filter(function() {

	    var val = $(this).css('background-image').replace(/url\(/g,'').replace(/\)/,'').replace(/"/g,'');
	    var imgVal = $(this).not('script').attr('src');

	    if(val !== 'none' && !/linear-gradient/g.test(val) && bgImg.indexOf(val) === -1){
	    	bgImg.push(val)
	    }

	    if(imgVal !== undefined && img.indexOf(imgVal) === -1){
	    	img.push(imgVal)
	    }

 	});

    var imgArray = bgImg.concat(img); 

    $.each(imgArray, function(i,val){ //Adding load and error event
		$("<img />").attr("src", val).bind("load", function () {
            completeImageLoading();
        });

        $("<img />").attr("src", val).bind("error", function () {
            imgError(this);
        });
    })

    function completeImageLoading(){
    	count++;
    	percentage = Math.floor(count / imgArray.length * 100);
    	Android.drawPercentage(percentage + '%');
    	if(percentage === 100){
			Android.completeToLoad();
    	}
    }

    //Error handling
    function imgError (arg) {
    	Android.failToLoad();
		//Android.completeToLoad();
    }

});