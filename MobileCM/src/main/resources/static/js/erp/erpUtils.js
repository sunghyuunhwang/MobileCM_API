/**
 * 
 */
function getORMList(funcSuccess, funcFail) {
	$.ajax({
	    url: "/v1/api/erp/getERMList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
	    data: "",
	    success: function(data){
	    	funcSuccess(data);
	    },
	    error: function (request, status, error){
	    	funcFail(request, status, error);
	    }
	});
}