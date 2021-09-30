/**
 * 
 */
function getCenterList(funcSuccess, funcFail) {
	$.ajax({
	    url: "/v1/api/tms/centerList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
	    data: "",
	    success: function(data){
	    	funcSuccess(data);
	    },
	    error: function (request, status, error){
//	    	alert(error+ "e");
	    	funcFail(request, status, error);
	    }
	  });
}

function getVehicleList(funcSuccess, funcFail) {
	$.ajax({
	    url: "/v1/api/tms/vehicleList",
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

function getOrderList(funcSuccess, funcFail) {
	$.ajax({
	    url: "/v1/api/tms/orderList",
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

function deleteCenter(centerId, funcSuccess, funcFail) {
	$.ajax({
	    url: "/v1/api/tms/centerDelete",
	    type: "GET",
	    cache: false,
	    dataType: "json",
	    data: {centerId : centerId },
	    success: function(data){
	    	funcSuccess(data);
	    },
	    error: function (request, status, error){
	    	funcFail(request, status, error);
	    }
	  });
}

function allDeleteCenter(funcSuccess, funcFail) {
	$.ajax({
	    url: "/v1/api/tms/allCenterDelete",
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

function vehicleListInsert(funcSuccess, funcFail) {
	$.ajax({
	    url: "/v1/api/tms/vehicleListInsert",
	    type: "POST",
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

function orderListUpdateFromErp(funcSuccess, funcFail) {
	$.ajax({
	    url: "/v1/api/tms/orderListUpdateFromErp",
	    type: "POST",
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

function allocation(allocationType, orderIdList, vehicleIdList, startTime, funcSuccess, funcFail) {
	$.ajax({
	    url: "/v1/api/tms/allocation",
	    type: "GET",
	    cache: false,
	    dataType: "json",
	    data: {
	    	allocationType : allocationType,
	    	orderIdList : orderIdList,
	    	vehicleIdList : vehicleIdList,
	    	startTime : startTime
	    },
	    success: function(data){
	    	funcSuccess(data);
	    },
	    error: function (request, status, error){
	    	funcFail(request, status, error);
	    }
	  });
}