function getAttachFileList() {//미결 파일대리점 반품
     $.ajax({
        url: "/v1/api/tmserp/getAttachFileList",
        type: "GET",
        cache: false,
        dataType: "json",
         data: {
                 attach_file_id:'I20211002010301'
        },
        success: function(list){
            $.each(list, function(idx, response) {
                  var getFileList = "<li><a href='' class='bfafNn icnDown'>"+response.file_nm+"</a> </li>";
                   $('#getAttachFileList').append(getFileList);
               console.log(response);
              });
                 cmma();//콤마
                 innmbr();//인풋값 스팬으로 넘기기
        },
         //complete:function{

         //},
        error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
        }
      });
}
function getVndBanpum() {//대리점 반품
var start = $('.vndBanpumStatus #nmldate1').datepicker('getDate');
var end   = $('.vndBanpumStatus #nmldate2').datepicker('getDate');
if(!start || !end)
    return;
var days = (end - start)/1000/60/60/24;
 if(days >= 30){
	 $('.alrtPop').addClass('opn');
	 $('#vndBSs_popFail').addClass('on');
 }else{
	 $.ajax({
 	    url: "/v1/api/tmserp/getVndBanpum",
 	    type: "GET",
 	    cache: false,
 	    dataType: "json",
          data: {
          	   from_dt: '20210910',
              to_dt:'20211020'
 	    },
 	    success: function(list){
 		    $.each(list, function(idx, response) {

 			    var getVndBanpum = "<ul class='ulLftlst'>";
 			        getVndBanpum += "<li class='w110px'>"+response.plm_cdt+"</li>";
 			        getVndBanpum += "<li class='w150px tAlgnCntr'>"+response.sti_nm+"</li>";
 			        getVndBanpum += "<li class='w100px'>"+response.com_brand_nm+"</li>";
 			        getVndBanpum += "<li class='w150px'>"+response.vnd_nm+"</li>";
 			        getVndBanpum += "<li class='w200px'>"+response.orm_nm+"</li>";
 			        getVndBanpum += "<li class='w150px'>"+response.orm_no+"</li>";
 			        getVndBanpum += "<li class='w100px'>"+response.set_cd+"</li>";
 			        getVndBanpum += "<li class='w100px'>"+response.col_scd+"</li>";
 			        getVndBanpum += "<li class='w130px'>"+response.itm_cd+"</li>";
 			        getVndBanpum += "<li class='w100px'>"+response.col_cd+"</li>";
 			        getVndBanpum += "<li class='w100px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value="+response.ord_qty+" class='innmbr nmCmma w100p'></li>";
 			        getVndBanpum += "<li class='w100px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value="+response.wtp_planqty+" class='innmbr nmCmma w100p'></li>";
 			        getVndBanpum += "<li class='w130px tAlgnRght'>"+response.wtp_finish_nm+"</li>";
 			        getVndBanpum += "<li class='w200px'>"+response.wtp_enddt+"</li>";
 			        getVndBanpum += "<li class='w100px tAlgnCntr'><button class='vndBSsBtn'>"+response.com_rdsec_nm+"</button></li>";
 			        getVndBanpum += "<li class='w130px tAlgnRght'>"+response.com_undsec_nm+"</li>";
 			        getVndBanpum += "</ul>";
 	  		     $('#getVndBanpumLst').append(getVndBanpum);
 			 console.log(response);
  	          });
                  cmma();//콤마
                  innmbr();//인풋값 스팬으로 넘기기
                  vndBSsFilePop();//첨부파일
 	    },
          //complete:function{

          //},
 	    error: function (request, status, error){
               console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
               alert('데이터를 불러올수 없습니다.');
 	    }
 	  });
    }


}


function cnstrctLst() {//시공건 검색 리스트
var from_dt = $('.cnstrctLstBx #nmldate1').val();//시작일
var to_dt = $('.cnstrctLstBx #nmldate2').val();//종료일
var orm_nm = $('input[name=orm_nm]').val();//건명
var itm_cd = $('input[name=itm_cd]').val();//단품코드
var start = $('.cnstrctLstBx #nmldate1').datepicker('getDate');
var end   = $('.cnstrctLstBx #nmldate2').datepicker('getDate');
if(!start || !end)
    return;
var days = (end - start)/1000/60/60/24;
 if((orm_nm.length < 1 && itm_cd.length < 1) || days >= 30){
	 $('.alrtPop').addClass('opn');
	 $('#resList_popFail').addClass('on');
 }else{
	 $.ajax({
 	    url: "/v1/api/tmserp/getResmstList",
 	    type: "GET",
 	    cache: false,
 	    dataType: "json",
          data: {
          	   from_dt: '20210910',
              to_dt:'20211020',
              com_scd: 'C16YA',
              ksti_cd: 'YA521',
              orm_nm: '(임)윤미주',
              itm_cd: 'DASDQEW2'
 	    },
 	    success: function(list){
 		    $.each(list, function(idx, response) {
 	                   var rem_dt = response.rem_dt;
 			         var com_ssec = response.com_ssec;
 	                   var com_brand = response.com_brand;
 	                   var orm_no = response.orm_no;
 	                   var orm_nm = response.orm_nm;
 	                   var orm_amt = response.orm_amt;
 	                   var orm_addr = response.orm_addr;
 	                   var sti_nm = response.sti_nm;

 			    var cnstrctLst = "<ul class='ulLftlst' onclick='cnstrctLst_dtlInf()'>";
 			        cnstrctLst += "<li class='w110px'>"+response.rem_dt+"</li>";
                        cnstrctLst += "<li class='w100px'>"+response.com_ssec+"</li>";
                        cnstrctLst += "<li class='w100px'>"+response.com_brand+"</li>";
                        cnstrctLst += "<li class='w150px'>"+response.orm_no+"</li>";
                        cnstrctLst += "<li class='w300px'>"+response.orm_nm+"</li>";
                        cnstrctLst += "<li class='numTxt w150px'><input type='text' name='' value='"+response.orm_amt+"' class='innmbr nmCmma'></li>";
                        cnstrctLst += "<li class='w500px'>"+response.orm_addr+"</li>";
                        cnstrctLst += "<li class='w150px'>"+response.sti_nm+"</li>";
                        cnstrctLst += "</ul>";
 	  		     $('#cnstrctLst').append(cnstrctLst);
 			 console.log(response);
  	          });
                  cmma();//콤마
                  innmbr();//인풋값 스팬으로 넘기기
 	    },
          //complete:function{

          //},
 	    error: function (request, status, error){
               console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
               alert('데이터를 불러올수 없습니다.');
 	    }
 	  });
    }


}
function cnstrctLst_dtlInf() {//상세정보
	$.ajax({
	    url: "/v1/api/tmserp/getResdtlList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
         	   orm_no: 'I20211001000100'
	    },
	    success: function(list){
		    $.each(list, function(idx, response) {
	                   var itm_cd = response.itm_cd;
			         var col_cd = response.col_cd;
	                   var ord_qty = response.ord_qty;
	                   var itm_nm = response.itm_nm;
	                   var orm_amt = response.orm_amt;

			    var cnstrctLst_dtlInf = "<ul class='ulLftlst'>";
			        cnstrctLst_dtlInf += "<li class='w150px'>"+itm_cd+"</li>";
                       cnstrctLst_dtlInf += "<li class='w150px tAlgnCntr'>"+col_cd+"</li>";
                       cnstrctLst_dtlInf += "<li class='w100px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value='"+ord_qty+"' class='innmbr nmCmma w100p'></li>";
                       cnstrctLst_dtlInf += "<li class='wCal550px'>"+itm_nm+"</li>";
                       cnstrctLst_dtlInf += "<li class='w150px tAlgnRght'><span class='numTxt'></span><input type='text' name='' value='"+orm_amt+"' class='innmbr nmCmma w100p'></li>";
                       cnstrctLst_dtlInf += "</ul>";
	  		     $('#cnstrctLst_dtlInf').append(cnstrctLst_dtlInf);
			 console.log(response);
 	          });
                 cmma();//콤마
                 innmbr();//인풋값 스팬으로 넘기기
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
	  });

}
