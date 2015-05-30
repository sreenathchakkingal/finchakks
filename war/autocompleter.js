$(document).ready(function() {
        $(function() {
                $("#stockName").autocomplete({     
                source : function(request, response) {
                $.ajax({
                        url : "autoComplete",
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
