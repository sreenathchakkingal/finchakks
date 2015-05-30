$(document).ready(function() {
        $(function() {
                $(".ratingValue").autocomplete({     
                source : function(request, response) {
                $.ajax({
                        url : "validRatingsAutoComplete",
                        type : "GET",
                        data : {
                                term : request.term
                        },
                        dataType : "json",
                        success : function(data) {
                                response(data);
                        }
                });
        }
});
});
});