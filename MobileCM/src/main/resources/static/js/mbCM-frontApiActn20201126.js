//모바일CM 프론트엔드 api관련 이벤트
//작업자 이커머스팀 : 심민아
//백엔드에 필요한 이벤트는 함수 혹은 함수안의 이벤트를 사용해 주시고
//백엔드에 사용한 프론트엔드이벤트가 $(document).ready(function()에서 필요 없을경우 주석 처리해주세요.*주석 처리시설명필수표시
function assgnCll(sqty,samt,constcst_sum) {//총건수 불러오기
     var dateapi = $('#date2').val();//현재 조정한 날짜
	$.ajax({
	    url: "/v1/api/tmserp/erp_SelectSigongAverageInfo",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
              time: dateapi
	    },
	    success: function(response){
              sqty = response.sqty;
              samt = response.samt;
              const_amt = response.const_amt;
              console.log(response);
	    	   $("#sqty").val(sqty);
             $("#samt").val(samt);
             $("#const_amt").val(const_amt);
              cmma();
              innmbr();
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
	  });
}
function lalngCll(resultCode,resultCount,resultMessage) {//위도경도
     $('.alrtPop').addClass('opn');
     $('#lodingPop').addClass('on');
     var dateapi = $('#date2').val();//현재 조정한 날짜
     $.ajax({
	    url: "/v1/api/tmserp/tmserp_UpdateOrderGeocoding",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
//              appkey: "l7xx965cfaee1f4c47608284f1271eccb662",
//              k_sti_cd: "YA142",
              rem_dt: dateapi
	    },
	    success: function(response){
                  resultCode = response.resultCode;
                  resultCount = response.resultCount;
                  resultMessage = response.resultMessage;
                 mbCMmapCll();//스케줄 불러오기
                $('.alrtPop').removeClass('opn');
                $('#lodingPop').removeClass('on');

	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
	  });

}

function mbCMmapCll(rem_dt,com_scd,sti_cd,sti_nm,orm_qty,orm_amt, constcst_sum,resultCode,resultCount,resultMessage) {//지도좌표 불러오기
     var dateapi = $('#date2').val();//현재 조정한 날짜
	$.ajax({
	    url: "/v1/api/tmserp/erp_SelectTeamSigongList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
//              com_scd: "C16YA",
//              sti_cd: "YA142",
              time: dateapi
	    },
	    success: function(list,response){
                  resultCode = response.resultCode;
                  resultCount = response.resultCount;
                  resultMessage = response.resultMessage;
		    $.each(list, function(idx, response) {
	                   var rem_dt = response.rem_dt;
	                   var com_scd = response.com_scd;
	                   var sti_cd = response.sti_cd;
	                   var sti_nm = response.sti_nm;
	                   var orm_qty = response.orm_qty;
	                   var orm_amt = response.orm_amt;
	                   var constcst_sum = response.constcst_sum;
			    var assgn_lst = "<li>";
			        assgn_lst += "<dl class='dfDl assgnBx nrrwwid _nrrw'>";
    			        assgn_lst += "<dt class='fao'><span class='crlclOn'></span>";
    			        assgn_lst += "<span class='txt'>"+response.sti_nm+"</span><input type='checkbox' class='chck' id='chks_"+idx+"' name='fromsti' value = '"+response.sti_cd+"'/><label for='chks_"+idx+"'></label>";
    			        assgn_lst += "</dt>";
    			        assgn_lst += "<dd>";
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>건수</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>건<input type='hidden' value="+response.orm_qty+" class='innmbr nmCmma'/>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>수주</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>원<input type='hidden' value="+response.orm_amt+" class='innmbr nmCmma'/>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>지급</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>원<input type='hidden' value="+response.constcst_sum+" class='innmbr nmCmma'/>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "</li>";
	  		     $('#assgn_lst').append(assgn_lst);
			 console.log(response);
 	          });
	    	      var mapCllBtn = $('.mapCll');
	           var assgnStrtBtn = $('.assgnStrt');
	           var scdlLst = $('.scdlLstBox');

	           mapCllBtn.removeClass('on');
	           assgnStrtBtn.addClass('on');
	           scdlLst.addClass('on');
                 cmma();//콤마
                 innmbr();//인풋값 스팬으로 넘기기
		      assgnLstCkck();// 할당리스트 클릭시
		      onoffBxCls(); // 박스 열고닫기
		      inNrrwWid();//f리스트 스타일 바꾸기
                $('.alrtPop').removeClass('opn');
                $('#lodingPop').removeClass('on');

	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
	  });
}

function assgnLstCll(rem_dt,com_scd,sti_cd,sti_nm,orm_qty,orm_amt, constcst_sum) {//배분리스트불러오기(체크박스)
     var pop = $('.pop_assgnStrtBx');
     pop.addClass('opn');
     var dateapi = $('#date2').val();//현재 조정한 날짜
     
     var sticd_list_from ='';
     
	$('input:checkbox[name="fromsti"]:checked').each(function () {
		sticd_list_from += this.value + ',';
	});    
		
	console.log(sticd_list_from);      
     allChck();
	$.ajax({
	    url: "/v1/api/tmserp/erp_stiteamAllList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
              time: dateapi
	    },
	    success: function(list){
		    $.each(list, function(idx, response) {
	                   var rem_dt = response.rem_dt;
	                   var com_scd = response.com_scd;
	                   var sti_cd = response.sti_cd;
	                   var sti_nm = response.sti_nm;
	                   var orm_qty = response.orm_qty;
	                   var orm_amt = response.orm_amt;
	                   var constcst_sum = response.constcst_sum;
			    var assgnPop_lst = "<li>";
			        assgnPop_lst += "<dl class='dfDl assgnBx'>";
    			        assgnPop_lst += "<dt class='fao'><span class='crlclOn'></span>"+response.sti_nm+"<input type='checkbox' class='chck' name='vehicles' value='"+response.sti_cd+"'/><input type='checkbox' class='chck' name='fromsti' value='"+sticd_list_from+"'/></dt>";
    			        assgnPop_lst += "<dd>";
    			        assgnPop_lst += "<dl class='dlLft'>";
    			        assgnPop_lst += "<dt>건수</dt>";
    			        assgnPop_lst += "<dd class='fltRght'>";
    			        assgnPop_lst += "<span class='numTxt'></span>건<input type='hidden' value="+response.orm_qty+" class='innmbr nmCmma'/>";
    			        assgnPop_lst += "</dd>";
    			        assgnPop_lst += "</dl>";
    			        assgnPop_lst += "<dl class='dlLft'>";
    			        assgnPop_lst += "<dt>수주</dt>";
    			        assgnPop_lst += "<dd class='fltRght'>";
    			        assgnPop_lst += "<span class='numTxt'></span>원<input type='hidden' value="+response.orm_amt+" class='innmbr nmCmma'/>";
    			        assgnPop_lst += "</dd>";
    			        assgnPop_lst += "</dl>";
    			        assgnPop_lst += "<dl class='dlLft'>";
    			        assgnPop_lst += "<dt>지급</dt>";
    			        assgnPop_lst += "<dd class='fltRght'>";
    			        assgnPop_lst += "<span class='numTxt'></span>원<input type='hidden' value="+response.constcst_sum+" class='innmbr nmCmma'/>";
    			        assgnPop_lst += "</dd>";
    			        assgnPop_lst += "</dl>";
    			        assgnPop_lst += "</dd>";
    			        assgnPop_lst += "</dl>";
    			        assgnPop_lst += "</li>";
	  		        $('#assgnPop_lst').append(assgnPop_lst);
			 console.log(response);
 	          });
                 cmma();//콤마
                 innmbr();//인풋값 스팬으로 넘기기
			  $('.pop_assgnStrtBx').append('<script>assgnLstCkck();allChck();chckLnth();</script>');
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
	  });
}
var mytime;
function tmserp_allocation() {//분배하기
     var pop = $('.pop_assgnStrtBx');
     pop.removeClass('opn');
     $('.alrtPop').addClass('opn');
     $('#lodingSchdlPop').addClass('on');
     tmsrpAllcStrt();
     mytime = setInterval(tmsrpAllcRply,60000);
}
function tmsrpAllcStrt(resultCode,resultCount,resultMessage,mappingKey) {//분배하기
     var dateapi = $('#date2').val();//현재 조정한 날짜
     
	 var sticd_list = '';
	 var sticd_list_from = '';
		
	$('input:checkbox[name="vehicles"]:checked').each(function () {
		sticd_list += this.value + ',';
	});
	
	console.log(sticd_list);
	
	$('input:checkbox[name="vehicles"]:checked').each(function () {
		sticd_list_from = $('input[name=fromsti]').val();
	});
		
	console.log(sticd_list_from);     
	
     $.ajax({
	    url: "/v1/api/tmserp/tmserp_Allocation",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
         	  sti_cd_area_arr:sticd_list_from,
         	  sti_cd_arr:sticd_list,
              rem_dt:dateapi,
              start_time:'0900'
	    },
	    success: function(response){
              resultCode = response.resultCode;
              resultCount = response.resultCount;
              resultMessage = response.resultMessage;
              mappingKey = response.mappingKey;
			 console.log(response);

	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
    });
}

function tmsrpAllcRply(resultCode,resultCount,resultMessage,mappingKey) {//1분단위요청
     var dateapi = $('#date2').val();//현재 조정한 날짜
     
	 var sticd_list = '';
	 var sticd_list_from = '';
		
	$('input:checkbox[name="vehicles"]:checked').each(function () {
		sticd_list += this.value + ',';
	});
	
	console.log(sticd_list);
	
	$('input:checkbox[name="vehicles"]:checked').each(function () {
		sticd_list_from = $('input[name=fromsti]').val();
	});
		
	console.log(sticd_list_from);
	     
     $.ajax({
	    url: "/v1/api/tmserp/tmserp_AllocationUpdate",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
//         	  appKey:"l7xx965cfaee1f4c47608284f1271eccb662",
//            k_sti_cd:"YA142",
//            sti_cd_area_arr:"YA284,YA035",
//            sti_cd_arr:"YA142,YA217,YA234,YA256,YA223",
//            rem_dt:dateapi,
//            usr_cd:"jds1014"

         	  sti_cd_area_arr:sticd_list_from,
         	  sti_cd_arr:sticd_list,
              rem_dt:dateapi,
              start_time:'0900'
			  usr_cd:''

	    },
	    success: function(response){
              resultCode = response.resultCode;
              resultCount = response.resultCount;
              resultMessage = response.resultMessage;
              mappingKey = response.mappingKey;
              if(resultCode == 200){
                 clearInterval(mytime);
                 $('.alrtPop').removeClass('opn');
                 $('#lodingSchdlPop').removeClass('on');
                 tmsrpAllctnLst();
              }
			 console.log(response);
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
    });
}
function tmsrpAllcRply(resultCode,resultCount,resultMessage,mappingKey) {//1분단위요청
     var dateapi = $('#date2').val();//현재 조정한 날짜
     
     console.log('1111111'+mappingKey);
	 var sticd_list = '';
	 var sticd_list_from = '';
		
	$('input:checkbox[name="vehicles"]:checked').each(function () {
		sticd_list += this.value + ',';
	});
	
	console.log(sticd_list);
	
	$('input:checkbox[name="vehicles"]:checked').each(function () {
		sticd_list_from = $('input[name=fromsti]').val();
	});
		
	console.log(sticd_list_from);     
       
     
     $.ajax({
	    url: "/v1/api/tmserp/tmserp_AllocationUpdate",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
//         	  appKey:"l7xx965cfaee1f4c47608284f1271eccb662",
//            k_sti_cd:"YA142",
//            sti_cd_area_arr:"YA284,YA035",
//            sti_cd_arr:"YA142,YA217,YA234,YA256,YA223",
//            rem_dt:dateapi,
//            usr_cd:"jds1014"
			  
			  mappingKey:mappingKey,
         	  sti_cd_area_arr:sticd_list_from,
         	  sti_cd_arr:sticd_list,
              rem_dt:dateapi,
			  usr_cd:''
			  
	    },
	    success: function(response){
              resultCode = response.resultCode;
              resultCount = response.resultCount;
              resultMessage = response.resultMessage;
              mappingKey = response.mappingKey;
              if(resultCode == 200){
                 clearInterval(mytime);
                 $('.alrtPop').removeClass('opn');
                 $('#lodingSchdlPop').removeClass('on');
                 tmsrpAllctnLst();
              }
			 console.log(response);
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
    });
}

function tmsrpAllctnLst(rem_dt,com_scd,sti_cd,sti_nm,orm_qty,orm_amt, constcst_sum) {//분배결과리스트
     var dateapi = $('#date2').val();//현재 조정한 날짜
	$('#assgn_lst li').remove();
	$.ajax({
	    url: "/v1/api/tmserp/erp_SelectTeamSigongList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
              time:dateapi
	    },
	    success: function(list){
		    $.each(list, function(idx, response) {
	                   var rem_dt = response.rem_dt;
	                   var com_scd = response.com_scd;
	                   var sti_cd = response.sti_cd;
	                   var sti_nm = response.sti_nm;
	                   var orm_qty = response.orm_qty;
	                   var orm_amt = response.orm_amt;
	                   var constcst_sum = response.constcst_sum;
			    var assgn_lst = "<li>";
			        assgn_lst += "<dl class='dfDl assgnBx nrrwwid _nrrw'>";
    			        assgn_lst += "<dt class='fao'><span class='crlclOn'></span>";
    			        assgn_lst += "<span class='txt' onclick='assgnSubLst(this)'>"+response.sti_nm+"<input type='hidden' value="+response.sti_cd+" name = 'sti_cd'/></span><input type='checkbox' class='chck' id='chks_"+idx+"'/><label for='chks_"+idx+"'></label>";
    			        assgn_lst += "</dt>";
    			        assgn_lst += "<dd>";
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>건수</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>건<input type='hidden' value="+response.orm_qty+" class='innmbr nmCmma'/>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>수주</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>원<input type='hidden' value="+response.orm_amt+" class='innmbr nmCmma'/>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>지급</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>원<input type='hidden' value="+response.constcst_sum+" class='innmbr nmCmma'/>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "</li>";
	  		     $('#assgn_lst').append(assgn_lst);
			 console.log(response);
 	          });
		      $('#assgn_lst').addClass('assgnDn');
	    	      var mapCllBtn = $('.mapCll');
	           var assgnStrtBtn = $('.menuBox .assgnStrt');
	           var scdlLst = $('.scdlLstBox');

	           mapCllBtn.removeClass('on');
	           assgnStrtBtn.addClass('on');
	           scdlLst.addClass('on');
                assgnStrtBtn.removeClass('btnBlck');
                assgnStrtBtn.addClass('btnBlckLn');
                 cmma();//콤마
                 innmbr();//인풋값 스팬으로 넘기기
		      assgnLstCkck();// 할당리스트 클릭시
		      onoffBxCls(); // 박스 열고닫기
		      inNrrwWid();//f리스트 스타일 바꾸기
			 $('.scdlLstBox').append('<script>assgnsubLstChk();assgnsubInf();</script>');

	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
	  });
}

//assgnDn
function assgnSubLst(_this, com_ssec,com_brand,rem_dt,rem_seq,plm_no,orm_no,orm_nm,orm_gaddr, ord_amt) {//상세리스트

var queryString = $(_this).find("[name=sti_cd]").val();
var dateapi = $('#date2').val();//현재 조정한 날짜
$('#assgnSub_lst li').remove();
	$.ajax({
	    url: "/v1/api/tmserp/erp_SelectSigongAsList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
         	    sti_cd:queryString,
              time:dateapi
	    },
	    success: function(list){
		    $.each(list, function(idx, response) {
	                   var com_ssec = response.com_ssec;
			         var com_brand = response.com_brand;
	                   var rem_dt = response.rem_dt;
	                   var rem_seq = response.rem_seq;
	                   var plm_no = response.plm_no;
	                   var orm_no = response.orm_no;
	                   var orm_nm = response.orm_nm;
	                   var orm_gaddr = response.orm_gaddr;
	                   var ord_amt = response.ord_amt;
	                   var wallfix_yn = response.wallfix_yn;
	                   var cannot_one = response.cannot_one;
	                   var trash_yn = response.trash_yn;
	                   var rem_ptm = response.rem_ptm;
	                   var rem_rmk = response.rem_rmk;
	                   var latitude = response.latitude;//위도
	                   var longitude = response.longitude;//경도
			    var assgn_lst = "<li onclick='assgnsubInf()'>";
			        assgn_lst += "<span class='crlclOn'></span>";
    			        assgn_lst += "<dl class='dfDl assgnSbInfBx'>";
    			        assgn_lst += "<dt class='fao'>";
    			        assgn_lst += "<span class='txt' onclick='assgninfLstdt(this)' data-plm-no='"+response.plm_no+"' data-com-ssec='"+response.com_ssec+"'>"+response.orm_nm+"</span>";
    			        assgn_lst += "<ul class='chkIcnLst iss'>";
    			        assgn_lst += "<li class='issIcn_3'>"+response.cannot_one+"</li>";
    			        assgn_lst += "<li class='issIcn_4'>"+response.wallfix_yn+"</li>";
    			        assgn_lst += "<li class='issIcn_8'>"+response.trash_yn+"</li>";
    			        assgn_lst += "</ul>";
    			        assgn_lst += "</dt>";
    			        assgn_lst += "<dd>";
    			        assgn_lst += "<address class='assgnSbAddInf'>"+response.orm_gaddr+"</address>";
    			        assgn_lst += "<span class='comSsec'>"+response.com_ssec+"</span>";
    			        assgn_lst += "<span class='numTxt'></span><input type='hidden' value="+response.ord_amt+" class='innmbr nmCmma'/>";
    			        assgn_lst += "<span class='txtIcn fllGry'>"+response.com_brand+"</span>";
    			        assgn_lst += "<span class='assgnRqtime'>"+response.rem_ptm+"</span>";
    			        assgn_lst += "<span class='assgnRqtxt'>"+response.rem_rmk+"</span>";
                       assgn_lst += "<input type='hidden' value="+response.latitude+" class='latitudeNbr' />"
                       assgn_lst += "<input type='hidden' value="+response.longitude+" class='longitudeNbr' />"
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "</li>";
	  		     $('#assgnSub_lst').append(assgn_lst);
				issonoff();
			 console.log(response);
 	          });
                 cmma();//콤마
                 innmbr();//인풋값 스팬으로 넘기기
                 addMarkersTooMuch(list);
			  $('#assgnSub_lst').append('<script>assgnsubLstChk();assgnsubInf();</script>');
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
	  });

}


function chngTmStrt(sti_cd,sti_nm) {//팀변경
     assgnsubInfOnly();
	$.ajax({
	    url: "/v1/api/erp/selectStiList2",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
//              k_sti_cd:"YA521"
	    },
	    success: function(list){
		    $.each(list, function(idx, response) {
	                   var sti_cd = response.sti_cd;
	                   var sti_nm = response.sti_nm;
	  		     $('#chngTmLst').append("<li>"+response.sti_nm+"</li>");
				$('#chsAssgnInfBx').append('<script>chngTmLst();</script>');
			 console.log(response);
 	          });
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }

	  });
}

function chngTmDon(resultCode,resultCount,resultMessage) {//팀 변경 완료
     var dateapi = $('#date2').val();//현재 조정한 날짜
     $.ajax({
	    url: "/v1/api/erp/erp_updateSticd",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
              sti_cd:"YA223",
              com_ctsec:"C06L",
              com_scd:"C16YA",
              rem_dt:dateapi,
              rem_seq:"IC0068",
              plm_no:"",
              orm_no:"I20201011080200",
              sti_nm:"(YA_수원(영통)",
              com_ssec:"C18C"
	    },
	    success: function(response){
              resultCode = response.resultCode;
              resultCount = response.resultCount;
              resultMessage = response.resultMessage;
              $('.alrtPop').removeClass('opn');
              $('#chngTmDonPop').removeClass('on');
              var donNm = $('.chngTm').text();
              $('.assgnTm .fao').text(donNm);
              $('.chngTmBx1').removeClass('chng');
              $('.chngTmLstBx').removeClass('chng');
              $('.chngTmBtn').removeClass('chng');
              $('#assgnInfLstPop').addClass('opn');
              $('#assgnInfLstPop').addClass('smll');
              tmsrpAllctnLst();//분배결과리스트 다시 불러오기
               allRset();//리셋(프론트엔드부분만)
		    console.log(response);
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }

	  });

}

function assgninfLstdt(_this,rem_dt,com_scd,sti_cd,sti_nm,orm_qty,orm_amt, constcst_sum) {//시공품목리스트

	var plm_no2 = $(_this).attr("data-plm-no");
	var com_ssec2 = $(_this).attr("data-com-ssec");
	
    $('#assgnInfLstPop .infLstdtBx ul').remove();
    
	$.ajax({
	    url: "/v1/api/tmserp/erp_selectsigongitemlist",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
           plm_no:plm_no2,
           com_ssec:com_ssec2
	    },
	    success: function(list){
		    $.each(list, function(idx, response) {
	                   var com_pldsec = response.com_pldsec;
	                   var itmcd_col = response.itmcd_col;
	                   var ord_amt = response.ord_amt;
	                   var pld_fqty = response.pld_fqty;
	                   var itm_nm = response.itm_nm;
	                   var pld_sum = response.pld_sum;
                        var assgn_lst = "<ul class='dfBx flxBtwn infLstdata'>";
     			        assgn_lst += "<li class='w78px'>"+response.com_pldsec+"</li>";
         			        assgn_lst += "<li class='w125px'>"+response.itmcd_col+"</li>";
         			        assgn_lst += "<li class='w270px'>"+response.itm_nm+"</li>";
         			        assgn_lst += "<li class='w45px'>"+response.pld_fqty+"</li>";
         			        assgn_lst += "<li class='w100px'><span class='numTxt'></span><input type='hidden' value="+response.ord_amt+" class='innmbr nmCmma'/></li>";
         			        assgn_lst += "<li class='w100px'><span class='numTxt'></span><input type='hidden' value="+response.pld_sum+" class='innmbr nmCmma'/></li>";
         			        assgn_lst += "</ul>";
     	  		     $('#assgnInfLstPop .infLstdtBx').append(assgn_lst);
			 console.log(response);
 	          });
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }

	  });
}

function allctnconfirm() {//분배확정 버튼모양 변경,팝업닫기이벤트
          $('.assgnDon').addClass('don');
          $('.alrtPop').removeClass('opn');
          $('#assgnDonPop').removeClass('on');
          $('.mapCll').removeClass('on');
          $('.assgnStrt').removeClass('on');
          $('.rsvtnStrt').addClass('on');
}

function rsvtnconfirm() {//예약확정후 프론트엔드 이벤트
          $('.assgnDon').removeClass('don');
          $('.assgnDon').addClass('off');
          $('.alrtPop').removeClass('opn');
          $('#rsvtnStrtPop').removeClass('on');
          $('.mapCll').removeClass('on');
          $('.assgnStrt').removeClass('on');
          $('.rsvtnStrt').removeClass('on');
          $('.scdlAllDon').addClass('on');
}
$(document).ready(function(){
     if($('.loginBx').length < 1){
          assgnCll();//총건수 불러오기
     }
});