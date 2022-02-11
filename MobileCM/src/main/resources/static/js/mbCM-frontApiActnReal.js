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
                //$('.alrtPop').removeClass('opn');
                //$('#lodingPop').removeClass('on');

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
	                   var as_count = response.as_count;
	                   var real_cnt = response.real_cnt;
			    var assgn_lst = "<li>";
			        assgn_lst += "<dl class='dfDl assgnBx nrrwwid _nrrw'>";
    			        assgn_lst += "<dt class='fao'><span class='crlclOn'></span>";
    			        assgn_lst += "<span class='txt' onclick='assgnSubLst(this)'>"+response.sti_nm+"<input type='hidden' value="+response.sti_cd+" name = 'sti_cd'/></span><input type='checkbox' class='chck' id='chks_"+idx+"' name='fromsti' value = '"+response.sti_cd+"'/><label for='chks_"+idx+"'></label>";
    			        assgn_lst += "</dt>";
    			        assgn_lst += "<dd>";
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>건수</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>건<input type='hidden' value="+response.orm_qty+" class='innmbr nmCmma'/>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>실건수</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>건<input type='hidden' value="+response.real_cnt+" class='innmbr nmCmma'/>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";    
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>AS건수</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>건<input type='hidden' value="+response.as_count+" class='innmbr nmCmma'/>";
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
                //$('.alrtPop').removeClass('opn');
                //$('#lodingPop').removeClass('on');

	    },
         complete:function(){
                $('.alrtPop').removeClass('opn');
                $('#lodingPop').removeClass('on');
         },
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
     $('#assgnPop_lst li').remove();
     var sticd_list_from ='';

	$('input:checkbox[name="fromsti"]:checked').each(function () {
		sticd_list_from += this.value + ',';
	});

	var from_sti_check = sticd_list_from.length;

    if (from_sti_check == 0) {
    	alert("분배 대상 팀을 선택하세요.(체크박스선택)");
    	return false;
    }

	console.log(sticd_list_from);
     allChck();
     $('.pop_assgnStrtBx .ttlMenu li .numTxt').text('0');
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
                 assgnLstCkck();
                 onoffBxCls(); // 박스 열고닫기
 		       inNrrwWid();//f리스트 스타일 바꾸기
                 allChck();
                 chckLnth();
                 assgnsubInf();
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
	  });
}
var mytime = "";
function tmserp_allocation() {//분배하기
     var pop = $('.pop_assgnStrtBx');
     pop.removeClass('opn');
     $('.alrtPop').addClass('opn');
     $('#lodingSchdlPop').addClass('on');
     tmsrpAllcStrt();
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
	if(mytime != "") {
		clearInterval(mytime);
		mytime = "";
	}

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
              var resultCode = response.resultCode;
              var resultCount = response.resultCount;
              var resultMessage = response.resultMessage;
              var mappingKey = response.mappingKey;
			 console.log(response);
			 mytime = setInterval(function(){tmsrpAllcRply(resultCode,resultCount,resultMessage,mappingKey)},10000);
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
                 if(mytime != "") {
					clearInterval(mytime);
					mytime = "";
				}
              }else if (resultCode == 5001) {
              	if(mytime != "") {
					clearInterval(mytime);
					mytime = "";
				}
				$('.alrtPop').removeClass('opn');
                $('#lodingSchdlPop').removeClass('on');
                alert("오류가 발생하였습니다.\r\n 다시 시도해 주시기 바랍니다.");
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
	                   var as_count = response.as_count;
	                   var real_cnt = response.real_cnt;
	                    
			    var assgn_lst = "<li>";
			        assgn_lst += "<dl class='dfDl assgnBx nrrwwid _nrrw'>";
    			        assgn_lst += "<dt class='fao'><span class='crlclOn'></span>";
//    			        assgn_lst += "<span class='txt' onclick='assgnSubLst(this)'>"+response.sti_nm+"<input type='hidden' value="+response.sti_cd+" name = 'sti_cd'/></span><input type='checkbox' class='chck' id='chks_"+idx+"'/><label for='chks_"+idx+"'></label>";
    			        assgn_lst += "<span class='txt' onclick='assgnSubLst(this)'>"+response.sti_nm+"<input type='hidden' value="+response.sti_cd+" name = 'sti_cd'/></span><input type='checkbox' class='chck' id='chks_"+idx+"' name='fromsti' value = '"+response.sti_cd+"'/><label for='chks_"+idx+"'></label>";
    			        assgn_lst += "</dt>";
    			        assgn_lst += "<dd>";
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>건수</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>건<input type='hidden' value="+response.orm_qty+" class='innmbr nmCmma'/>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>실건수</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>건<input type='hidden' value="+response.real_cnt+" class='innmbr nmCmma'/>";
    			        assgn_lst += "</dd>";
    			        assgn_lst += "</dl>";    
    			        assgn_lst += "<dl class='dlLft'>";
    			        assgn_lst += "<dt>AS건수</dt>";
    			        assgn_lst += "<dd class='fltRght'>";
    			        assgn_lst += "<span class='numTxt'></span>건<input type='hidden' value="+response.as_count+" class='innmbr nmCmma'/>";
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
function sortAssgnSub_lst() {//상세리스트 정렬

var sti_cd = $('.clck').find("[name=sti_cd]").val(); // 시공팀 코드
var sort_type = $('select[name=sort_type]').val();
var sort_seq = $('select[name=sort_seq]').val();
var dateapi = $('#date2').val();//현재 조정한 날짜
$('#assgnSub_lst li').remove();
	$.ajax({
	    url: "/v1/api/tmserp/erp_SelectSigongAsList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
         	  sti_cd: sti_cd,
              time:dateapi,
              sort_type: sort_type,
              sort_seq: sort_seq
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
	                   var constcst_sum = response.constcst_sum;
					   var vnd_nm = response.vnd_nm;
					   var schdiv_yn = response.schdiv_yn;
	                   var amt2 = constcst_sum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
//					   var amt2 = constcst_sum;
	                   var text = "지급금액 : ";
	                   var text2 = " 원";
	                   var com_rfg = response.com_rfg;
					   var req_time = response.req_time;
					   var req_loc = response.req_loc;
	                   var arr_change_data = "";
	                   arr_change_data = com_ssec+"/"+rem_dt+"/"+rem_seq+"/"+plm_no+"/"+orm_no

			    var assgn_lst = "<li onclick='assgninfLstdt(this)'>";
			        assgn_lst += "<span class='crlclOn'></span>";
    			        assgn_lst += "<dl class='dfDl assgnSbInfBx'>";
    			        assgn_lst += "<dt class='fao'>";
    			        assgn_lst += "<input type='checkbox' name='fromsti2' id='chks_"+idx+"' value = '"+arr_change_data+"' /><span class='txt' data-plm-no='"+response.plm_no+"' data-com-ssec='"+response.com_ssec+"' >"+response.orm_nm+"</span>";
    			        assgn_lst += "<ul class='chkIcnLst iss'>";
    			        assgn_lst += "<li class='issIcn_3'>"+response.cannot_one+"</li>";
    			        assgn_lst += "<li class='issIcn_4'>"+response.wallfix_yn+"</li>";
    			        assgn_lst += "<li class='issIcn_8'>"+response.trash_yn+"</li>";
    			        assgn_lst += "</ul>";
    			        assgn_lst += "</dt>";
    			        assgn_lst += "<dd>";
    			        assgn_lst += "<address class='assgnSbAddInf'>"+response.orm_gaddr+"</address>";
    			        assgn_lst += "<span class='comSsec'>"+response.com_ssec+"<input type='hidden' name='from_com_ssec' value="+response.com_ssec+" /><input type='hidden' name='from_rem_dt' value="+response.rem_dt+" /><input type='hidden' name='from_rem_seq' value="+response.rem_seq+" /><input type='hidden' name='from_plm_no' value="+response.plm_no+" /><input type='hidden' name='from_orm_no' value="+response.orm_no+" /></span>";
    			        assgn_lst += "<span class='numTxt'></span><input type='hidden' value="+response.ord_amt+" class='innmbr nmCmma'/>";
    			        assgn_lst += "<span class='txtIcn fllGry'>"+response.com_brand+"</span>";
    			        assgn_lst += "<span class='assgnRqtime'>"+response.rem_ptm+" </span>";
    			        assgn_lst += "<span class='assgnRqtxt'>"+response.rem_rmk+"</span>";
    			        assgn_lst += "<span class='assgnRqgiveamt'><font color = 'blue'><b>"+amt2+"</b></font>"+text2+"</span>";
    			        assgn_lst += "<span class='assgnRqagt'>"+vnd_nm+"</span>";
    			        assgn_lst += "<span class='assgnRqschdivyn'>"+schdiv_yn+"</font></span>";
    			        assgn_lst += "<span class='assgnRqcomrfg'>"+com_rfg+"</span>";
    			        assgn_lst += "<span class='assgnReqTime'>"+req_time+"</span>";
    			        assgn_lst += "<span class='assgnReqLoc'>"+req_loc+"</span>";
    			        assgn_lst += "<input type='hidden' value="+com_rfg+" name='comrfg'/>"
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
			  $("input:checkbox[id='allcheck']").prop("checked", false); /* by ID */
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
var sort_type = $('select[name=sort_type]').val();
var sort_seq = $('select[name=sort_seq]').val(); 
$('#assgnSub_lst li').remove();
	$.ajax({
	    url: "/v1/api/tmserp/erp_SelectSigongAsList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
         	    sti_cd:queryString,
              time:dateapi,
              sort_type: sort_type,
              sort_seq: sort_seq
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
	                   var constcst_sum = response.constcst_sum;
					   var vnd_nm = response.vnd_nm;
					   var schdiv_yn = response.schdiv_yn;
	                   var amt2 = constcst_sum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
//					   var amt2 = constcst_sum;
	                   var text = "지급금액 : ";
	                   var text2 = " 원";
	                   var com_rfg = response.com_rfg;
					   var req_time = response.req_time;
					   var req_loc = response.req_loc;
	                   var arr_change_data = "";
	                   arr_change_data = com_ssec+"/"+rem_dt+"/"+rem_seq+"/"+plm_no+"/"+orm_no

			    var assgn_lst = "<li onclick='assgninfLstdt(this)'>";
			        assgn_lst += "<span class='crlclOn'></span>";
    			        assgn_lst += "<dl class='dfDl assgnSbInfBx'>";
    			        assgn_lst += "<dt class='fao'>";
    			        assgn_lst += "<input type='checkbox' name='fromsti2' id='chks_"+idx+"' value = '"+arr_change_data+"' /><span class='txt' data-plm-no='"+response.plm_no+"' data-com-ssec='"+response.com_ssec+"' >"+response.orm_nm+"</span>";
    			        assgn_lst += "<ul class='chkIcnLst iss'>";
    			        assgn_lst += "<li class='issIcn_3'>"+response.cannot_one+"</li>";
    			        assgn_lst += "<li class='issIcn_4'>"+response.wallfix_yn+"</li>";
    			        assgn_lst += "<li class='issIcn_8'>"+response.trash_yn+"</li>";
    			        assgn_lst += "</ul>";
    			        assgn_lst += "</dt>";
    			        assgn_lst += "<dd>";
    			        assgn_lst += "<address class='assgnSbAddInf'>"+response.orm_gaddr+"</address>";
    			        assgn_lst += "<span class='comSsec'>"+response.com_ssec+"<input type='hidden' name='from_com_ssec' value="+response.com_ssec+" /><input type='hidden' name='from_rem_dt' value="+response.rem_dt+" /><input type='hidden' name='from_rem_seq' value="+response.rem_seq+" /><input type='hidden' name='from_plm_no' value="+response.plm_no+" /><input type='hidden' name='from_orm_no' value="+response.orm_no+" /></span>";
    			        assgn_lst += "<span class='numTxt'></span><input type='hidden' value="+response.ord_amt+" class='innmbr nmCmma'/>";
    			        assgn_lst += "<span class='txtIcn fllGry'>"+response.com_brand+"</span>";
    			        assgn_lst += "<span class='assgnRqtime'>"+response.rem_ptm+" </span>";
    			        assgn_lst += "<span class='assgnRqtxt'>"+response.rem_rmk+"</span>";
    			        assgn_lst += "<span class='assgnRqgiveamt'><font color = 'blue'><b>"+amt2+"</b></font>"+text2+"</span>";
    			        assgn_lst += "<span class='assgnRqagt'>"+vnd_nm+"</span>";
    			        assgn_lst += "<span class='assgnRqschdivyn'>"+schdiv_yn+"</font></span>";
    			        assgn_lst += "<span class='assgnRqcomrfg'>"+com_rfg+"</span>";
    			        assgn_lst += "<span class='assgnReqTime'>"+req_time+"</span>";
    			        assgn_lst += "<span class='assgnReqLoc'>"+req_loc+"</span>";    			        
    			        assgn_lst += "<input type='hidden' value="+com_rfg+" name='comrfg' />"
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
			  $("input:checkbox[id='allcheck']").prop("checked", false); /* by ID */
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
	                   var com_scd = response.com_scd;
	                   var com_ctsec = response.com_ctsec;
	  		     $('#chngTmLst').append("<li>"+response.sti_nm+"<input type='hidden' name='from_com_scd' value="+response.com_scd+" /><input type='hidden' name='from_com_ctsec' value="+response.com_ctsec+" /><input type='hidden' name='from_sti_nm' value="+response.sti_nm+" /><input type='hidden' name='from_sti_cd' value="+response.sti_cd+" /></li>");
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

function chngKTmStrt(sti_cd,sti_nm) {//권역외변경
     assgnsubInfOnly();
	$.ajax({
	    url: "/v1/api/erp/selectKStiList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
//              com_scd :"C16YA"
	    },
	    success: function(list){
		    $.each(list, function(idx, response) {
	                   var sti_cd = response.sti_cd;
	                   var sti_nm = response.sti_nm;
	                   var com_scd = response.com_scd;
	                   var com_ctsec = response.com_ctsec;
	  		     $('#chngTmLst').append("<li>"+response.sti_nm+"<input type='hidden' name='from_com_scd' value="+response.com_scd+" /><input type='hidden' name='from_com_ctsec' value="+response.com_ctsec+" /><input type='hidden' name='from_sti_nm' value="+response.sti_nm+" /><input type='hidden' name='from_sti_cd' value="+response.sti_cd+" /></li>");
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

	var $selected_to = $("#chngTmLst li.on");

	var dateapi = $('#date2').val();//현재 조정한 날짜

	var from_com_scd = $selected_to.find('input[name=from_com_scd]').val();
	var from_com_ctsec = $selected_to.find('input[name=from_com_ctsec]').val();
	var from_sti_nm = $selected_to.find('input[name=from_sti_nm]').val();
	var from_sti_cd = $selected_to.find('input[name=from_sti_cd]').val();


	$('input:checkbox[name="fromsti2"]:checked').each(function () {

		var from_com_ssec = $(this).closest('li').find('input[name=from_com_ssec]').val();
		var from_rem_dt = $(this).closest('li').find('input[name=from_rem_dt]').val();
		var from_rem_seq = $(this).closest('li').find('input[name=from_rem_seq]').val();
		var from_plm_no = $(this).closest('li').find('input[name=from_plm_no]').val();
		var from_orm_no = $(this).closest('li').find('input[name=from_orm_no]').val();

		$.ajax({
	    url: "/v1/api/tmserp/erp_updateSticd",
	    type: "GET",
	    cache: false,
	    async: false,
	    dataType: "json",
         data: {
         	  login_id:from_sti_cd,
              sti_cd:from_sti_cd,
              com_ctsec:from_com_ctsec,
              com_scd:from_com_scd,
              rem_dt:from_rem_dt,
              rem_seq:from_rem_seq,
              plm_no:from_plm_no,
              orm_no:from_orm_no,
              sti_nm:from_sti_nm,
              com_ssec:from_com_ssec,
              treq_time:'0900'
	    },
	    success: function(response){
              resultCode = response.resultCode;
              resultCount = response.resultCount;
              resultMessage = response.resultMessage;

		    console.log(response);
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }

	  });

	});

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

}

function assgninfLstdt(_this,rem_dt,com_scd,sti_cd,sti_nm,orm_qty,orm_amt, constcst_sum) {//시공품목리스트
    assgnsubInf();
	var plm_no2 = $(_this).find('.assgnSbInfBx .txt').attr("data-plm-no");
	var com_ssec2 = $(_this).find('.assgnSbInfBx .txt').attr("data-com-ssec");
	$('#assgnInfLstPop').removeClass('smll');//품목리스트 숨기기
    $('#assgnInfLstPop .infLstdtBx ul').remove();
	$.ajax({
	    url: "/v1/api/tmserp/erp_SelectSigongItemList",
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

	                   ord_amt = ord_amt.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	                   pld_sum = pld_sum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");

                        var assgn_lst = "<ul class='dfBx flxBtwn infLstdata'>";
     			        assgn_lst += "<li class='w78px'>"+response.com_pldsec+"</li>";
         			        assgn_lst += "<li class='w125px'>"+response.itmcd_col+"</li>";
         			        assgn_lst += "<li class='w270px'>"+response.itm_nm+"</li>";
         			        assgn_lst += "<li class='w45px'>"+response.pld_fqty+"</li>";
         			        assgn_lst += "<li class='w100px'>"+ord_amt+"<span class='numTxt'></span><input type='hidden' value="+response.ord_amt+" class='innmbr nmCmma'/></li>";
         			        assgn_lst += "<li class='w100px'>"+pld_sum+"<span class='numTxt'></span><input type='hidden' value="+response.pld_sum+" class='innmbr nmCmma'/></li>";
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

function selectAll(selectAll)  {
  const checkboxes 
     = document.querySelectorAll('input[name ="fromsti2"]');
  
  checkboxes.forEach((checkbox) => {
    checkbox.checked = selectAll.checked
  });
  $('#chsAssgnInfBx').addClass('opn');
  var vInfTmNm = $('#chsAssgnInfBx .assgnTm');
  var tmNm = $('.assgnLst .clck > .assgnBx .fao .txt');
  vInfTmNm.find('.fao').empty();
  vInfTmNm.find('.fao').text(tmNm.text());
  $('#chsAssgnInfBx .faoCtmrNm').text('');
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
	} else {
		resetUlLftlst();
		//resetUlLftdtllst();
		$('.alrtPop').addClass('opn');
		$('#lodingPop').addClass('on');
		$.ajax({
		    url: "/v1/api/tmserp/getResmstList",
		    type: "GET",
		    cache: false,
		    dataType: "json",
	        data: {
	         	 //from_dt: '20210910',
	             //to_dt:'20211020',
	             //com_scd: 'C16YA',
	             //ksti_cd: 'YA521',
	             //orm_nm: '(임)윤미주',
	             //itm_cd: 'DASDQEW2'
	         	 from_dt: from_dt,
	             to_dt:to_dt,
	             //com_scd: 'C16YA',
	             //ksti_cd: 'YA521',
	             orm_nm: orm_nm,
	             itm_cd: itm_cd
		    },
		    success: function(list){
			    $.each(list, function(idx, response) {
		                   var rem_dt = response.rem_dt;
				           var com_ssec_nm = response.com_ssec_nm;
		                   var com_brand = response.com_brand;
		                   var orm_no = response.orm_no;
		                   var orm_nm = response.orm_nm;
		                   var orm_amt = response.orm_amt;
		                   var orm_addr = response.address;
		                   var sti_nm = response.sti_nm;
		                   var com_ssec = response.com_ssec;
		                   var rpt_no =  response.rpt_no;
		                   var rpt_seq = response.rpt_seq;
	
				    //var cnstrctLst = "<ul class='ulLftlst' onclick='cnstrctLst_dtlInf()'>";
				    var cnstrctLst = "<ul class='ulLftlst'>";
				           //cnstrctLst += "<li class='w110px nmldatepicker'>"+response.rem_dt+"</li>";
				           cnstrctLst += "<li class='w100px'><input type='text' class='dateformat' value='"+response.rem_dt+"' readonly/></li>";
	                       cnstrctLst += "<li class='w100px'>"+response.com_ssec_nm+"</li>";
	                       cnstrctLst += "<li class='w100px'>"+response.com_brand+"</li>";
	                       cnstrctLst += "<li class='w150px'>"+response.orm_no+"</li>";
	                       cnstrctLst += "<li class='w150px'>"+response.sti_nm+"</li>";
	                       cnstrctLst += "<li class='w300px'>"+response.orm_nm+"</li>";
	                       cnstrctLst += "<li class='w150px'><span class='numTxt'></span><input type='text' name='' value='"+response.orm_amt+"' class='innmbr nmCmma w100p'></li>";
	                       cnstrctLst += "<li class='w500px'>"+response.address+"</li>";
	                       cnstrctLst += "<input type='hidden' name = 'com_ssec' value='"+com_ssec+"'/>";
	                       cnstrctLst += "<input type='hidden' name = 'orm_no' value='"+orm_no+"'/>";
	                       cnstrctLst += "<input type='hidden' name = 'rpt_no' value='"+rpt_no+"'/>";
	                       cnstrctLst += "<input type='hidden' name = 'rpt_seq' value='"+rpt_seq+"'/>";
	                       cnstrctLst += "</ul>";
		  		     $('#cnstrctLst').append(cnstrctLst);
				 console.log(response);
	 	          });
	                 cmma();//콤마
	                 innmbr();//인풋값 스팬으로 넘기기
	                 dateformatting();
	                 ulLftlst();
	                 if(list.length > 0){
		                var datalist = $('#cnstrctLst ul.ulLftlst').not('._index');
		                $(datalist[0]).addClass('on');
		                cnstrctLst_dtlInf(datalist[0]);
	                }
		     },
	         complete:function(){
	                $('.alrtPop').removeClass('opn');
	                $('#lodingPop').removeClass('on');
	         },
		    error: function (request, status, error){
	              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	              alert('데이터를 불러올수 없습니다.');
		    }
		  });
	}
}

function cnstrctLst_dtlInf(_this) {//상세정보
	
	resetUlLftdtllst();
	var $selected_to = $(_this);
	var $selected_com_ssec = $selected_to.find('input[name=com_ssec]').val();
	var $selected_orm_no = $selected_to.find('input[name=orm_no]').val();
	var $selected_rpt_no = $selected_to.find('input[name=rpt_no]').val();
	var $selected_rpt_seq = $selected_to.find('input[name=rpt_seq]').val();
	
	$.ajax({
	    url: "/v1/api/tmserp/getResdtlList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
			   com_ssec: $selected_com_ssec,
         	   orm_no: $selected_orm_no,
         	   rpt_no: $selected_rpt_no,
         	   rpt_seq: $selected_rpt_seq
	    },
	    success: function(list){
		    $.each(list, function(idx, response) {
	                   var itm_cd = response.itm_cd;
			           var col_cd = response.col_cd;
	                   var ord_qty = response.ord_qty;
	                   var itm_nm = response.itm_nm;
	                   var itm_cst = response.itm_cst;
	                   var all_cst = response.all_cst;

			    var cnstrctLst_dtlInf = "<ul class='ulLftlst'>";
			        cnstrctLst_dtlInf += "<li class='w150px'>"+itm_cd+"</li>";
                    cnstrctLst_dtlInf += "<li class='w150px tAlgnCntr'>"+col_cd+"</li>";
                    cnstrctLst_dtlInf += "<li class='w100px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value='"+ord_qty+"' class='innmbr nmCmma w100p'></li>";
                    cnstrctLst_dtlInf += "<li class='w150px tAlgnRght'><span class='numTxt'></span><input type='text' name='' value='"+itm_cst+"' class='innmbr nmCmma w100p'></li>";
                    cnstrctLst_dtlInf += "<li class='w150px tAlgnRght'><span class='numTxt'></span><input type='text' name='' value='"+all_cst+"' class='innmbr nmCmma w100p'></li>";
                    cnstrctLst_dtlInf += "<li class='wCal550px'>"+itm_nm+"</li>";
                    cnstrctLst_dtlInf += "</ul>";
	  		     $('#cnstrctLst_dtlInf').append(cnstrctLst_dtlInf);
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

	var from_dt = $('.vndBanpumStatus #nmldate1').val();//시작일
	var to_dt = $('.vndBanpumStatus #nmldate2').val();//종료일
	var start = $('.vndBanpumStatus #nmldate1').datepicker('getDate');
	var end   = $('.vndBanpumStatus #nmldate2').datepicker('getDate');
	if(!start || !end) return;
	var days = (end - start)/1000/60/60/24;
	if(days >= 100){
		 $('.alrtPop').addClass('opn');
		 $('#vndBSs_popFail').addClass('on');
	} else {
		resetUlLftlst();
		$('.alrtPop').addClass('opn');
		$('#lodingPop').addClass('on');
		$.ajax({
	 	    url: "/v1/api/tmserp/getVndBanpum",
	 	    type: "GET",
	 	    cache: false,
	 	    dataType: "json",
	        data: {
				from_dt: from_dt,
	            to_dt: to_dt
	 		},
	 	    success: function(list){
				$.each(list, function(idx, response) {
	                var file_yn = response.file_yn;
	 			    var getVndBanpum = "<ul class='ulLftlst'>";
				        getVndBanpum += "<li class='w100px'><input type='text' class='dateformat' value='"+response.plm_cdt+"' readonly/></li>";
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
	 			        getVndBanpum += "<li class='w200px'>"+response.wtp_entdt+"</li>";
	 			    if(file_yn == "Y") {
						getVndBanpum += "<li class='w100px tAlgnCntr'><button class='inFileBtn' onclick='inFileBtnPop(this)'>"+response.com_rdsec_nm+"</button></li>";		
					}else{
						getVndBanpum += "<li class='w100px tAlgnCntr'>"+response.com_rdsec_nm+"</li>";						
					}    
	 			        getVndBanpum += "<li class='w130px tAlgnRght'>"+response.com_undsec_nm+"</li>";
	                    getVndBanpum += "<input type='hidden' name = 'plm_no' value='"+response.plm_no+"'/>";	 			        
	 			        getVndBanpum += "</ul>";
	 	  		     $('#getVndBanpumLst').append(getVndBanpum);
	  	          });
	                  cmma();//콤마
	                  innmbr();//인풋값 스팬으로 넘기기
	                  dateformatting();
	                  ulLftlst();
	                  /*inFileBtnPop();//첨부파일*/
	 	    },
	         complete:function(){
	                $('.alrtPop').removeClass('opn');
	                $('#lodingPop').removeClass('on');
	         },
	 	    error: function (request, status, error){
	               console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	               alert('데이터를 불러올수 없습니다.');
	 	    }
	 	  });
	    }
}

function getAttachFileList(plm_no, file_id) { // 대리점반품 파일
	 resetFilelst();
     $.ajax({
        url: "/v1/api/tmserp/getAttachFileList",
        type: "GET",
        cache: false,
        dataType: "json",
        data: {
			plm_no: plm_no,
			file_id : file_id
        },
        success: function(list){
            $.each(list, function(idx, response) {
                  var getFileList = "<ul class='ulLftlst'>";
                  	  getFileList += "<li><button class='bfafNn icnDown'>"+response.real_attch_file_name+"</button></li>";
	                  getFileList += "<input type='hidden' name = 'file_id' value='"+response.attch_file_id+"'/>";
	                  getFileList += "<input type='hidden' name = 'file_snum' value='"+response.attch_file_snum+"'/>";
	                  getFileList += "</ul>";
                  $('#getAttachFileList').append(getFileList);
              });
                 cmma();//콤마
                 innmbr();//인풋값 스팬으로 넘기기
                 vndFileDown();
        },
         //complete:function{

         //},
        error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
        }
      });
}
function requestDownloadFile(reqObj) {
    if (!reqObj || !reqObj.url) {
        return;
    }
 
    var isGetMethod = reqObj.method && reqObj.method.toUpperCase() === 'GET';
    $.ajax({
        url: reqObj.url,
        method: isGetMethod ? 'GET' : 'POST',
        xhrFields: {
            responseType: 'arraybuffer'
        },
        data: $.param(reqObj.data) // a=1&b=2&c=3 방식
        // data: JSON.stringify(reqObj.data) // {a:1, b:2, c:3} JSON 방식
 
    }).done(function(data, textStatus, jqXhr) {
        if (!data) {
            return;
        }
        try {
            var blob = new Blob([data], { type: jqXhr.getResponseHeader('content-type') });
            var fileName = getFileName(jqXhr.getResponseHeader('content-disposition'));
            fileName = decodeURI(fileName);
 
            if (window.navigator.msSaveOrOpenBlob) { // IE 10+
                window.navigator.msSaveOrOpenBlob(blob, fileName);
            } else { // not IE
                var link = document.createElement('a');
                var url = window.URL.createObjectURL(blob);
                link.href = url;
                link.target = '_self';
                if (fileName) link.download = fileName;
                document.body.append(link);
                link.click();
                link.remove();
                window.URL.revokeObjectURL(url);
            }
        } catch (e) {
            console.error(e)
        }
    });
}
function getFileName (contentDisposition) {
    var fileName = contentDisposition
        .split(';')
        .filter(function(ele) {
            return ele.indexOf('filename') > -1
        })
        .map(function(ele) {
            return ele
                .replace(/"/g, '')
                .split('=')[1]
        });
    return fileName[0] ? fileName[0] : null
}

function fileDownload(file_no, file_snum) { // 파일 다운로드
	requestDownloadFile({
        url: "/v1/api/tmserp/fileDownload",
        type: "GET",	
        data: {
			file_id: file_no,
			file_snum: file_snum
        },	
	});
}
function getMglSubInf(_this) {//상세상세미결현황요약정보
     $('#getMglSubInf').find('.ulLftlst._data').remove();
   	 $('.ulLftlst').removeClass('on');
	 $(_this).addClass('on');
	 var $selected_to = $(_this);
	 var $selected_plm_no = $selected_to.find('input[name=plm_no]').val();
     $.ajax({
       url: "/v1/api/tmserp/getMigyeoDetaillnfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                plm_no: $selected_plm_no
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getMglSubInf = "<ul class='ulLftlst _data'>";
/*                   getMglSubInf += "<li class='w60px _lstNum'></li>";*/
                   getMglSubInf += "<li class='w150px'>"+response.com_pldsec_nm+"</li>";
                   getMglSubInf += "<li class='w150px'>"+response.itm_cd+"</li>";
                   getMglSubInf += "<li class='w150px'>"+response.col_cd+"</li>";
                   getMglSubInf += "<li class='w250px'>"+response.itm_nm+"</li>";
                   getMglSubInf += "<li class='w100px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value='"+response.pld_eqty+"' class='innmbr nmCmma w100p'></li>";
                   getMglSubInf += "<li class='w150px tAlgnRght'><span class='numTxt'></span><input type='text' name='' value='"+response.pld_famt+"' class='innmbr nmCmma w100p'></li>";
                   getMglSubInf += "<li class='w150px'>"+response.com_undsec_nm+"</li>";
                   getMglSubInf += "<li class='w300px'>"+response.pld_rmk+"</li>";
                   getMglSubInf += "</ul>";
                $('#getMglSubInf').append(getMglSubInf);
                   cmma();//콤마
                   innmbr();//인풋값 스팬으로 넘기기
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getMigyeolInfo(_this) {//상세미결현황요약정보
     $('.migyeolInfo').addClass('opn');
	 var from_dt = $('.migyeolReport #nmldate1').val();//시작일
	 var to_dt = $('.migyeolReport #nmldate2').val();//종료일
	 resetMigyeolUlLftlst();
	 var $selected_to = $(_this);
	 var $selected_sti_cd = $selected_to.find('input[name=sti_cd]').val();
     $('.alrtPop').addClass('opn');
	 $('#lodingPop').addClass('on');	
     $.ajax({
       url: "/v1/api/tmserp/getMigyeolnfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                from_dt: from_dt,
                to_dt: to_dt,
                sti_cd: $selected_sti_cd
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var mglRprtInf = "<ul class='ulLftlst _inmigyeolInf' onclick='getMglSubInf(this)'>";
                   mglRprtInf += "<li class='w150px'>"+response.sti_nm+"</li>";
/*                   mglRprtInf += "<li class='w100px'>"+response.com_ssec+"<input type='hidden' name='plm_no' value='"+response.plm_no+"' /></li>";
*/                   mglRprtInf += "<li class='w110px'>"+response.plm_cdt+"</li>";
                   mglRprtInf += "<li class='w100px'>"+response.com_brand_nm+"</li>";
                   mglRprtInf += "<li class='w150px'>"+response.vnd_nm+"</li>";
                   mglRprtInf += "<li class='w200px'>"+response.orm_nm+"</li>";
                   mglRprtInf += "<li class='w150px'>"+response.orm_no+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr _unpsecTxt'>"+response.unpsec_r_yn+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr _unpsecTxt'>"+response.unpsec_a_yn+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr _unpsecTxt'>"+response.unpsec_e_yn+"</li>";
                   mglRprtInf += "<li class='w100px tAlgnCntr _unpsecTxt'>"+response.unpsec_c_yn+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr'><button class='inFileBtn' onclick='inFileBtnPop(this)'>"+response.file_yn+"</button></li>";
                   mglRprtInf += "<li class='w300px'>"+response.mob_remark+"</li>";
                   mglRprtInf += "<input type='hidden' name = 'plm_no' value='"+response.plm_no+"'/>";
                   mglRprtInf += "</ul>";
                $('#getMigyeolInf').append(mglRprtInf);
                migyeolinfStl() ;//미경상세화면변경
            });
/*            inFileBtnPop();*/
	        if(list.length > 0){
		        var datalist = $('#getMigyeolInf ul.ulLftlst').not('._index');
		        $(datalist[0]).addClass('on');
		        getMglSubInf(datalist[0]);
	        }
       },
	   complete:function(){
		$('.alrtPop').removeClass('opn');
	    $('#lodingPop').removeClass('on');
	   },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}

function getMigyeolReportInfo() {//미결현황요약정보
	var from_dt = $('.migyeolReport #nmldate1').val();//시작일
	var to_dt = $('.migyeolReport #nmldate2').val();//종료일
	var start = $('.migyeolReport #nmldate1').datepicker('getDate');
	var end   = $('.migyeolReport #nmldate2').datepicker('getDate');	
	if(!start || !end) return;
	var days = (end - start)/1000/60/60/24;
	if(days >= 100){
		 $('.alrtPop').addClass('opn');
		 $('#migyeol_popFail').addClass('on');
	} else {
		resetUlLftlst();
		$('.alrtPop').addClass('opn');
		$('#lodingPop').addClass('on');
	    $.ajax({
	      url: "/v1/api/tmserp/getMigyeolReportInfo",
	      type: "GET",
	      cache: false,
	      dataType: "json",
	      data: {
	               from_dt: from_dt,
	               to_dt:to_dt
	      },
	      success: function(list) {
			//alert(JSON.stringify(list.allInfo));         
			var response = list.allInfo;
	           var tot_cnt = response.tot_cnt;
	           var comp_cnt = response.comp_cnt;
	           var migyeol_cnt = response.migyeol_cnt
	           var comp_per = response.comp_per;
	           var migyeol_per = response.migyeol_per;
	           var com_unpsec_r = response.com_unpsec_r;
	           var com_unpsec_a= response.com_unpsec_a;
	           var com_unpsec_e = response.com_unpsec_e;
	           var com_unpsec_c = response.com_unpsec_c;
	           var comp_unpsec_r_per = response.com_unpsec_r_per;
	           var comp_unpsec_a_per= response.com_unpsec_a_per;
	           var comp_unpsec_e_per = response.com_unpsec_e_per;
	           var comp_unpsec_c_per = response.com_unpsec_c_per;
	           $('.ttl._tot').find('.numCnt').text(tot_cnt);
	           $('.ttl._migyeol').find('.numCnt').text(migyeol_cnt);
	           $('.bar._comp').find('.numCnt').text(comp_cnt);
	           $('.bar._migyeol').find('.numCnt').text(migyeol_cnt);
	           $('.bar._comp').find('.numPer').text(comp_per);
	           $('.bar._migyeol').find('.numPer').text(migyeol_per);
	           $('.bar._unpsec_r').find('.numPer').text(comp_unpsec_r_per);
	           $('.bar._unpsec_a').find('.numPer').text(comp_unpsec_a_per);
	           $('.bar._unpsec_e').find('.numPer').text(comp_unpsec_e_per);
	           $('.bar._unpsec_c').find('.numPer').text(comp_unpsec_c_per);
	           $('.bar._unpsec_r').find('.numCnt').text(com_unpsec_r);
	           $('.bar._unpsec_a').find('.numCnt').text(com_unpsec_a);
	           $('.bar._unpsec_e').find('.numCnt').text(com_unpsec_e);
	           $('.bar._unpsec_c').find('.numCnt').text(com_unpsec_c);
	           $.each(list.info, function(idx, response) {			
	              var getMglRprtInfLst = "<ul class='ulLftlst'>";
	                  getMglRprtInfLst += "<li class='w150px'>"+response.sti_nm+"<input type='hidden' name='sti_cd' value='"+response.sti_cd+"' /></li>";
	                  getMglRprtInfLst += "<li class='w100px tAlgnRght fnt500'>"+response.tot_cnt+"</li>";
	                  getMglRprtInfLst += "<li class='w100px tAlgnRght' >"+response.comp_cnt+" ("+response.comp_per+"%)</li>";
	                  getMglRprtInfLst += "<li class='w100px tAlgnRght' >"+response.migyeol_cnt+" ("+response.migyeol_per+"%)</li>";
	                  getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_r'>"+response.com_unpsec_r+" ("+response.com_unpsec_r_per+"%)</li>";
	                  getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_a'>"+response.com_unpsec_a+" ("+response.com_unpsec_a_per+"%)</li>";
	                  getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_e'>"+response.com_unpsec_e+" ("+response.com_unpsec_e_per+"%)</li>";
	                  getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_c'>"+response.com_unpsec_c+" ("+response.com_unpsec_c_per+"%)</li>";
	                  getMglRprtInfLst += "</ul>";
	               $('#getMigyeolRprtInfLst').append(getMglRprtInfLst);
	           });
	           migyeolPerBar();
	           ulLftlst();
	           cmma();//콤마
	      },
	      complete:function(){
			$('.alrtPop').removeClass('opn');
	        $('#lodingPop').removeClass('on');
	      },
	      error: function (request, status, error){
	           console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	           alert('조회데이터가 없습니다.');
	      }
   		});
	}

}
function saveOpinion() {//이의제기 저장
     $(document).on("click",".saveOpnnBtn",function(){
           var rpt_no = $('.getDfctInfLst.on').find('._rpt_no').text();
           var rpt_seq = $('.getDfctInfLst.on').find('._rpt_seq').text();
           var bmt_item = $('.getDfctDetail.on').find('._bmt_item').text();
           var col_cd = $('.getDfctDetail.on').find('._col_cd').text();
           var opinion = $('#opinion').val();
         $.ajax({
               url: "/v1/api/tmserp/saveOpinion",
               type: "PUT",
               cache: false,
               dataType: "json",
               contentType: 'application/json',
               data:JSON.stringify ({
                    rpt_no: rpt_no,
                    rpt_seq: rpt_seq,
                    bmt_item: bmt_item,
                    col_cd: col_cd,                   
                    opinion: opinion
               }),
               success : function(data){   //파일 주고받기가 성공했을 경우. data 변수 안에 값을 담아온다.
                    alert("이의제기 내용이 저장되었습니다.");
                    $('.getDfctDetail.on').find('._opinion').text(opinion);
              }
         });
     });


}
function getDfctDetail(_this) {//하자내역상새조회
     $('#getDfctDetail').find('.ulLftlst._data').remove();
   	 $('.ulLftlst').removeClass('on');
	 $(_this).addClass('on');
	 var $selected_to = $(_this);
	 var $selected_rpt_no = $selected_to.find('input[name=rpt_no]').val();
 	 var $selected_rpt_seq = $selected_to.find('input[name=rpt_seq]').val();
     $.ajax({
       url: "/v1/api/tmserp/getDefectDetail",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                rpt_no: $selected_rpt_no,
                rpt_seq: $selected_rpt_seq
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getDfctDetail = "<ul class='ulLftlst _indefectInf _data getDfctDetail'>";
                   getDfctDetail += "<li class='w150px'>"+response.itm_cd+"</li>";
                   getDfctDetail += "<li class='w200px _bmt_item'>"+response.bmt_item+"</li>";
                   getDfctDetail += "<li class='w200px tAlgnCntr _col_cd'>"+response.col_cd+"</li>";
                   getDfctDetail += "<li class='w100px tAlgnCntr'>"+response.ast_actqty+"</li>";
                   getDfctDetail += "<li class='w100px tAlgnCntr'>"+response.ast_rtnyn+"</li>";
                   getDfctDetail += "<li class='dsplyNon _opinion'>"+response.opinion+"</li>";
                   getDfctDetail += "<li class='wCal550px'><button class='inFileBtn' onclick='inFileBtnPop(this)'>"+response.file_yn+"</button></li>";
	               getDfctDetail += "<input type='hidden' name ='file_id' value='"+response.final_file_id+"'/>";		                                   
                   getDfctDetail += "</ul>";
                $('#getDfctDetail').append(getDfctDetail);
            });

			var datalist = $('#getDfctDetail ul.ulLftlst').not('._index');
			$(datalist[0]).addClass('on');
		    var opinion = list[0].opinion;
		    if(opinion.length > 0){
		       $('#opinion').val(opinion);
		    }else{
		       $('#opinion').val('이의 제기내용이 없습니다.');
		    }
			defectinfStl();

		    /*inFileBtnPop();*/
       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getDfctInf() {//하자내역조회
	var from_dt = $('.defectInfo #nmldate1').val();//시작일
	var to_dt = $('.defectInfo #nmldate2').val();//종료일
	var start = $('.defectInfo #nmldate1').datepicker('getDate');
	var end   = $('.defectInfo #nmldate2').datepicker('getDate');
	var ctm_nm = $('input[name=ctm_nm]').val();//건명	
	if(!start || !end) return;
	var days = (end - start)/1000/60/60/24;
	if(days >= 100){
		 $('.alrtPop').addClass('opn');
		 $('#defectInfo_popFail').addClass('on');
	} else {
		resetUlLftlst();
		$('.alrtPop').addClass('opn');
		$('#lodingPop').addClass('on');
	    $.ajax({
	      url: "/v1/api/tmserp/getDefectInfoList",
	      type: "GET",
	      cache: false,
	      dataType: "json",
	      data: {
	               from_dt: from_dt,
	               to_dt: to_dt,
	               ctm_nm: ctm_nm
	      },
	      success: function(list){
	           $.each(list, function(idx, response) {
	              var getDfctInf = "<ul class='ulLftlst _indefectInf getDfctInfLst' onclick='getDfctDetail(this);'>";
	                  getDfctInf += "<li class='w110px'><input type='text' class='dateformat' value='"+response.rpt_enddt+"' readonly/></li>";
	                  getDfctInf += "<li class='w150px _rpt_no'>"+response.rpt_no+"</li>";
	                  getDfctInf += "<li class='w80px tAlgnCntr _rpt_seq'>"+response.rpt_seq+"</li>";
	                  getDfctInf += "<li class='w100px'>"+response.vnd_snm+"</li>";
	                  getDfctInf += "<li class='w300px tAlgnLft'>"+response.ctm_nm+"</li>";
	                  getDfctInf += "<li class='w150px'>"+response.sti_nm+"<input type='hidden' value='"+response.sti_cd+"' /></li>";
	                  getDfctInf += "<li class='w110px'><input type='text' class='dateformat' value='"+response.plm_cdt+"' readonly/></li>";
	                  getDfctInf += "<li class='w150px'>"+response.rpt_rst_acttm_nm+"<input type='hidden' value='"+response.rpt_rst_acttm+"' /></li>";
	                  getDfctInf += "<li class='w100px'>"+response.rpt_usrnm+"</li>";
	                  getDfctInf += "<li class='w80px tAlgnCntr'>"+response.rtnsec+"</li>";
	                  getDfctInf += "<li class='w80px tAlgnCntr'><button class='inFileBtn' onclick='inFileBtnPop(this)'>"+response.file_yn+"</button></li>";
	                  getDfctInf += "<li class='dsplyNon _rpt_astdesc'>"+response.rpt_astdesc+"</li>";
	                  getDfctInf += "<li class='dsplyNon _rpt_desc'>"+response.rpt_desc+"</li>";
	                  getDfctInf += "<input type='hidden' name = 'rpt_no' value='"+response.rpt_no+"'/>";
	                  getDfctInf += "<input type='hidden' name = 'rpt_seq' value='"+response.rpt_seq+"'/>";
	                  getDfctInf += "<input type='hidden' name = 'file_id' value='"+response.file_id+"'/>";
	                  getDfctInf += "<input type='hidden' name = 'plm_no' value='"+response.plm_no+"'/>";
	                  getDfctInf += "</ul>";
	               $('#getDfctInf').append(getDfctInf);
	           });
	           if(list.length > 0){
				   var datalist = $('#getDfctInf ul.ulLftlst').not('._index');
				   $(datalist[0]).addClass('on');
				   $('#rpt_astdesc').text(list[0].rpt_astdesc);
				   $('#rpt_desc').text(list[0].rpt_desc);
				   getDfctDetail(datalist[0]);
			   }
	           $('.getDfctInfLst').click(function() {
	              $('.ulLftlst').removeClass('on');
	              var rpt_astdesc = $(this).find('._rpt_astdesc').text();
	              var rpt_desc =  $(this).find('._rpt_desc').text();
	                if(rpt_astdesc.length > 0){
	                 $('#rpt_astdesc').text(rpt_astdesc);
	                }else{
	                  $('#rpt_astdesc').text('전달 사항이 없습니다.');
	                }
	                if(rpt_desc.length > 0){
	                   $('#rpt_desc').text(rpt_desc);
	                }else{
	                   $('#rpt_desc').text('요구 내역이 없습니다.');
	                }
/*	                if(opinion.length > 0){
	                   $('#opinion').text(opinion);
	                }else{
	                   $('#opinion').text('이의 제기내용이 없습니다.');
	                }*/
	             $(this).addClass('on');
	           });	
	           dateformatting();			
	      },
	      complete:function(){
			$('.alrtPop').removeClass('opn');
	        $('#lodingPop').removeClass('on');
	      },
	      error: function (request, status, error){
	           console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	           alert('데이터를 불러올수 없습니다.');
	      }
   		});
   	}
}
function delstimmbrChckPop(){//시공팀원정보삭제확인
     $(document).on("click",".delstimmbrChck",function(){
         $('.delstimmbrChckPop').addClass('opn');
         var stm_nm = $(".stiMmbrMdfPop._Mdf").find('._stm_nm').val();
         $('.alert_stm_nm').text(stm_nm);
    });
}
function delstimmbrinf(data){//시공팀원정보삭제
    $('.delstimmbrChckPop').removeClass('opn');
     let mmbrpop = $(".stiMmbrMdfPop._Mdf");
     let stm_no = mmbrpop.find('._stm_no').val();
     let com_scd = mmbrpop.find('._com_scd').val();
     let sti_cd = mmbrpop.find('._sti_cd').val();

     $.ajax({
            url: "/v1/api/tmserp/tmserp_deletestimemberinfo",
            type: "DELETE",
            cache: false,
            dataType: "json",
            data:{
            stm_no:stm_no,
            com_scd:com_scd,
            sti_cd:sti_cd
            },
            success : function(data){
                 alert("팀원 정보가 삭제되었습니다.");
                 mmbrpop.removeClass('opn _insrt');
                 //window.location.reload();
                 var clikUl = $('#getStiMemberInfo .on');
                 getStiMemberDetailInfo(clikUl);                 
          }
     });
}
function insrtstimmbrinf() {//시공팀원정보등록
     var mmbrpop = $(".stiMmbrMdfPop._insrt");
     var astm_addr = mmbrpop.find('.address_stm_addr').val();
     var dstm_addr = mmbrpop.find('#detailAddress').val();
     var stm_addr = mmbrpop.find('._stm_addr');
     stm_addr.val(astm_addr+dstm_addr);
     var stm_addrinf = mmbrpop.find('._stm_addr').val();
     var stm_nm = mmbrpop.find('._stm_nm').val();
     var stm_no = mmbrpop.find('._stm_no').val();
     var com_scd = mmbrpop.find('._com_scd').val();
     var sti_cd = mmbrpop.find('._sti_cd').val();
     var stm_hp = mmbrpop.find('._stm_hp').val();
     var com_pos = mmbrpop.find('._com_pos').attr("data-value");
     var stm_zip = mmbrpop.find('._stm_zip').val();
     var car_no = mmbrpop.find('._car_no').val();
     var stm_jdt = mmbrpop.find('._stm_jdt').val();

     var stimember = {
     stm_addr:stm_addrinf,
     stm_nm:stm_nm,
     stm_no:stm_no,
     com_scd:com_scd,
     sti_cd:sti_cd,
     stm_hp:stm_hp,
     com_pos:com_pos,
     stm_zip:stm_zip,
     car_no:car_no,
     stm_jdt:stm_jdt
     }
     if(stm_nm.length < 1 || (stm_jdt.length < 1 || isNaN(stm_jdt))){
          alert('팀원 이름/입사일은 필수입력 사항입니다. 입사일은 숫자 8자리로 입력해주세요. (예 : 20200909)');
          if(stm_nm.length < 1){
               mmbrpop.find('._stm_nm').addClass('wrng');
          }
          if(stm_jdt.length < 1 || isNaN(stm_jdt)){
              mmbrpop.find('._stm_jdt').addClass('wrng');
          }
     }else{
           mmbrpop.find('._stm_nm').removeClass('wrng');
           mmbrpop.find('._stm_jdt').removeClass('wrng');
          $.ajax({
                 url: "/v1/api/tmserp/tmserp_insertstimemberinfo",
                 type: "POST",
                 cache: false,
                 dataType: "json",
                 contentType: 'application/json',
                 data:{
                      stimember
                 },
                 data : JSON.stringify(stimember),
                 success : function(stimember){
                      alert("팀원 정보가 등록되었습니다.");
                      mmbrpop.removeClass('opn _insrt');
                      //window.location.reload();
	                  var clikUl = $('#getStiMemberInfo .on');
	                  getStiMemberDetailInfo(clikUl);
               }
          });
     }

}

function updtstimmbrinf() {//시공팀원정보수정
     let mmbrpop = $(".stiMmbrMdfPop._Mdf");
     let astm_addr = mmbrpop.find('.address_stm_addr').val();
     let dstm_addr = mmbrpop.find('#detailAddress').val();
     let stm_addr = mmbrpop.find('._stm_addr');
     stm_addr.val(astm_addr+dstm_addr);
     let stm_addrinf = mmbrpop.find('._stm_addr').val();
     let stm_nm = mmbrpop.find('._stm_nm').val();
     let stm_no = mmbrpop.find('._stm_no').val();
     let com_scd = mmbrpop.find('._com_scd').val();
     let sti_cd = mmbrpop.find('._sti_cd').val();
     let stm_hp = mmbrpop.find('._stm_hp').val().trim();
     let com_pos = mmbrpop.find('._com_pos').attr("data-value");
     let stm_zip = mmbrpop.find('._stm_zip').val();
     let car_no = mmbrpop.find('._car_no').val().trim();
     let stm_jdt = mmbrpop.find('._stm_jdt').val();
     var stimember = {
         stm_addr:stm_addrinf,
         stm_nm:stm_nm,
         stm_no:stm_no,
         com_scd:com_scd,
         sti_cd:sti_cd,
         stm_hp:stm_hp,
         com_pos:com_pos,
         stm_zip:stm_zip,
         car_no:car_no,
         stm_jdt:stm_jdt
     }

     if(stm_nm.length < 1 || (stm_jdt.length < 1 || isNaN(stm_jdt))){
          alert('팀원 이름/입사일은 필수입력 사항입니다. 입사일은 숫자 8자리로 입력해주세요. (예 : 20200909)');
          if(stm_nm.length < 1){
               mmbrpop.find('._stm_nm').addClass('wrng');
          }
          if(stm_jdt.length < 1 || isNaN(stm_jdt)){
              mmbrpop.find('._stm_jdt').addClass('wrng');
          }
     }else{
           mmbrpop.find('._stm_nm').removeClass('wrng');
           mmbrpop.find('._stm_jdt').removeClass('wrng');
       $.ajax({
              url: "/v1/api/tmserp/tmserp_updatestimemberinfo",
              type: "PUT",
              cache: false,
              dataType: "json",
              contentType: 'application/json',
              data:{
                   stimember
              },
              data : JSON.stringify(stimember),
              success : function(stimember){
                   alert("팀원 정보가 수정되었습니다.");
                   mmbrpop.removeClass('opn _Mdf');
                  // window.location.reload();
                  var clikUl = $('#getStiMemberInfo .on');
                  getStiMemberDetailInfo(clikUl);

            }
       });
     }

}

function getComcdList() {//시공팀직책리스트
     $.ajax({
       url: "/v1/api/tmserp/getComcdList",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
            cdx_cd:'C11'
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getCmcdLst = "<li class='slct'><span class='slctTxt _ccd_name' data-value='"+response.ccd_cd+"'>"+response.ccd_name+"</span></li>";
                $('#getComcdList').append(getCmcdLst);
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getStiMemberDetailInfo(e) {//시공팀원정보상세조회
     $('#getStiMemberDetailInfo > li').remove();
     let sti_cd = $(e).find('._sti_cd').text();
     let com_scd = $(e).find('._com_scd').val();
     let tmName = $(e).find('._com_scd').parents('li').text();
     let total_qty = $(e).find('._total_qty');
     $.ajax({
       url: "/v1/api/tmserp/getStiMemberDetailInfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
            sti_cd: sti_cd,
            com_scd:com_scd
       },
       success: function(list){
          if(total_qty.text() == '0'){
              $('#getStiMemberDetailInfo').append("<li class='nonData'>팀원정보가 없습니다.</li>");
          }else{
            $.each(list, function(idx, response) {
               var getStiMmbrDtl = "<li class='_stiMmbrDtil'>";
                   getStiMmbrDtl += "<dl class='inInfoDl wCal390px'><dt class='ttl'>팀원성명</dt><dd class='inf bld _stm_nm'>"+response.stm_nm+"<input type='hidden' value='"+response.stm_no+"' class='_stm_no'/><input type='hidden' value='"+response.sti_cd+"' class='_sti_cd'/><input type='hidden' value='"+com_scd+"' class='_com_scd'/></dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl w215px'><dt class='ttl'>전화번호</dt><dd class='inf bld _stm_hp'>"+response.stm_hp+"</dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl w175px'><dt class='ttl'>직책</dt><dd class='inf _com_pos'>"+response.com_pos+"<input type='hidden' value='"+response.com_pos_cd+"' class='_com_pos_cd'/></dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl wCal390px'><dt class='ttl'>우편번호</dt><dd class='inf _stm_zip'>"+response.stm_zip+"</dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl w215px'><dt class='ttl'>차량번호</dt><dd class='inf _car_no'>"+response.car_no+"</dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl w175px'><dt class='ttl'>입사일</dt><dd class='inf _stm_jdt'>"+response.stm_jdt+"</dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl w100p br0'><dt class='ttl'>주소</dt><dd class='inf _stm_addr'>"+response.stm_addr+"</dd></dl>";
                   getStiMmbrDtl += "</li>";
                    $('#getStiMemberDetailInfo').append(getStiMmbrDtl);
                inInfoDlwh();
                console.log(response);
            });
          }
          $('.stiMmbrInsrtBtn').addClass('on');
          $('.stiMmbrInsrtBtn').text(tmName + '팀의 팀원등록');
       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getStiMemberInfo() {//시공팀원정보현황조회
     $.ajax({
       url: "/v1/api/tmserp/getStiMemberInfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getStiMmbrInf = "<ul class='ulLftlst' onclick='getStiMemberDetailInfo(this);'>";
                   getStiMmbrInf += "<li class='w130px'>"+response.sti_nm+"<input type='hidden' value='"+response.com_scd+"' class='_com_scd'/></li>";
                   getStiMmbrInf += "<li class='w130px _sti_cd'>"+response.sti_cd+"</li>";
                   getStiMmbrInf += "<li class='w80px tAlgnCntr _total_qty'>"+response.total_qty+"</li>";
                   getStiMmbrInf += "<li class='w60px tAlgnCntr _car_qty'>"+response.car_qty+"</li>";
                   getStiMmbrInf += "</ul>";
                $('#getStiMemberInfo').append(getStiMmbrInf);
                ulLftlst();
                stiMmbrInfAllnmber();
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}

function stiResultStatus() {//시공팀별실적현황가져오기
	let fdt = $('#nmldate1').val();
	let tdt = $('#nmldate2').val();
	$('#stiResultStatus_1').find('.ulLftlst._data').remove();
	$('#stiResultStatus_2').find('.ulLftlst._data').remove();
	$('.alrtPop').addClass('opn');
	$('#lodingPop').addClass('on');
    $.ajax({
       url: "/v1/api/tmserp/getStiPerformInformation",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
          fdt:fdt,
          tdt:tdt
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var stiRStts_1 = "<ul class='ulLftlst _data'>";
                   stiRStts_1 += "<li class='w150px _cdata' data-stinm='"+response.sti_nm+"'>"+response.sti_nm+"<input type='hidden' value='"+response.sti_cd+"' class='_sti_cd'/></li>";
                   stiRStts_1 += "<li class='w120px'><span class='numTxt'></span><input type='text' name='' value='"+response.orm_amt+"' class='innmbr nmCmma _orm_amt'></li>";
                   stiRStts_1 += "<li class='w120px'><span class='numTxt'></span><input type='text' name='' value='"+response.constcst_sum+"' class='innmbr nmCmma _constcst_sum'></li>";
                   stiRStts_1 += "<li class='w85px tAlgnCntr _happycall_average'>"+response.happycall_average+"</li>";
                   stiRStts_1 += "<li class='w110px tAlgnCntr'>"+response.sigonghaja_average+"%("+response.sigonghaja_cnt+"건)</li>";
                   stiRStts_1 += "<li class='w110px tAlgnCntr'>"+response.inconsistent_average+"%("+response.total_sigong_cnt+"건)</li>";
                   stiRStts_1 += "<li class='w95px tAlgnCntr'>"+response.service_cnt+"</li>";
                   stiRStts_1 += "<li class='w90px tAlgnCntr'>"+response.claim_cnt+"</li>";
                   stiRStts_1 += "</ul>";
                $('#stiResultStatus_1').append(stiRStts_1);
            });
              ulLftlst();
               cmma();//콤마
               innmbr();//인풋값 스팬으로 넘기기

       },
       complete:function(){
			$('.alrtPop').removeClass('opn');
	        $('#lodingPop').removeClass('on');
	    },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });
   $.ajax({
    url: "/v1/api/tmserp/getStiDuedayInformation",
    type: "GET",
    cache: false,
    dataType: "json",
    data: {
        fdt:fdt,
        tdt:tdt
    },
    success: function(list){
          $.each(list, function(idx, response) {
             var stiRStts_2 = "<ul class='ulLftlst _inDpthClr _grn _data'>";
                 stiRStts_2 += "<li class='w150px _inDCr _cdata' data-stinm='"+response.sti_nm+"'>"+response.sti_nm+"<input type='hidden' value='"+response.sti_cd+"' class='_sti_cd'/></li>";
                 stiRStts_2 += "<li class='w80px tAlgnCntr _inDCr'>"+response.sti_qtycapa+"</li>";
                 stiRStts_2 += "<li class='w120px _inDCr'><span class='numTxt'></span><input type='text' name='' value='"+response.sti_amtcapa+"' class='innmbr nmCmma _orm_amt'></li>";
                 stiRStts_2 += "<li class='w80px tAlgnCntr _inDCr'>"+response.current_dueday+"</li>";
                 stiRStts_2 += "<li class='w110px'><span class='numTxt'></span><input type='text' name='' value='"+response.sti_currentamt+"' class='innmbr nmCmma _orm_amt'></li>";
                 stiRStts_2 += "<li class='w110px'><span class='numTxt'></span><input type='text' name='' value='"+response.avg_amt+"' class='innmbr nmCmma _orm_amt'></li>";
                 stiRStts_2 += "<li class='w95px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value='"+response.sum_currentqty+"' class='innmbr nmCmma _orm_amt'></li>";
                 stiRStts_2 += "<li class='w85px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value='"+response.avg_cnt+"' class='innmbr nmCmma _orm_amt'></li>";
                 stiRStts_2 += "<li class='w85px tAlgnCntr'>"+response.max_dueday+"</li>";
                 stiRStts_2 += "<li class='w85px tAlgnRght'>"+response.avg_dueday+"</li>";
                 stiRStts_2 += "</ul>";
              $('#stiResultStatus_2').append(stiRStts_2);
          });
             ulLftlst();
             valundefined();
             cmma();//콤마
             innmbr();//인풋값 스팬으로 넘기기

    },
    error: function (request, status, error){
          console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
          alert('데이터를 불러올수 없습니다.');
    }
   });
}

      
/*$(document).ready(function(){
     if($('.loginBx').length < 1){
          assgnCll();//총건수 불러오기
     }
});*/
