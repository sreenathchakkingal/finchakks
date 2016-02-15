$(document).ready(function() {
        $(function() {
                $("#bseId").autocomplete({     
                source : function(request, response) {
                $.ajax({
                        url : "autoCompleteBseId",
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
