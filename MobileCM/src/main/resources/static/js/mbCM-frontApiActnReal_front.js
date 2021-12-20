function saveOpinion() {//하자내역상새조회
     $(document).on("click",".saveOpnnBtn",function(){
           var rpt_no = $('.getDfctInfLst.on').find('_rpt_no').text();
           var rpt_seq = $('.getDfctInfLst.on').find('_rpt_seq').text();
           var opinion = $('#opinion').val();
         $.ajax({
               url: "/v1/api/tmserp/saveOpinion",
               type: "POST",
               cache: false,
               dataType: "json",
               contentType: 'application/json',
               data:{
                    rpt_no: rpt_no,
                    rpt_seq: rpt_seq,
                    opinion: opinion
               },
               data : JSON.stringify(data),
               success : function(data){   //파일 주고받기가 성공했을 경우. data 변수 안에 값을 담아온다.
                    alert("이의제기 내용이 저장되었습니다.");

              }
         });
     });


}
function getDfctDetail() {//하자내역상새조회
     $.ajax({
       url: "/v1/api/tmserp/getDefectDetail",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                rpt_no: 'I202109290263',
                rpt_seq: '01'
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getDfctDetail = "<ul class='ulLftlst'>";
                   getDfctDetail += "<li class='w150px'>"+response.itm_cd+"</li>";
                   getDfctDetail += "<li class='w200px'>"+response.bmt_item+"</li>";
                   getDfctDetail += "<li class='wCal680px tAlgnCntr'>"+response.col_cd+"</li>";
                   getDfctDetail += "<li class='w100px tAlgnCntr'>"+response.ast_actqty+"</li>";
                   getDfctDetail += "<li class='w80px  tAlgnCntr'><button class='inFileBtn'>"+response.file_yn+"</button></li>";
                   getDfctDetail += "</ul>";
                $('#getDfctDetail').append(getDfctDetail);
                inFileBtnPop();
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getDfctInf() {//하자내역조회
     $.ajax({
       url: "/v1/api/tmserp/getDefectInfoList",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                from_dt: '20210910',
                to_dt: '20211020',
                ctm_nm: ''
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getDfctInf = "<ul class='ulLftlst getDfctInfLst'>";
                   getDfctInf += "<li class='w110px'>"+response.rpt_enddt+"</li>";
                   getDfctInf += "<li class='w150px'><a class='lnkLn _rpt_no' onclick='getDfctDetail();'>"+response.rpt_no+"</a></li>";
                   getDfctInf += "<li class='w80px tAlgnCntr _rpt_seq'>"+response.rpt_seq+"</li>";
                   getDfctInf += "<li class='w300px'>"+response.vnd_snm+"</li>";
                   getDfctInf += "<li class='w100px tAlgnLft'>"+response.ctm_nm+"</li>";
                   getDfctInf += "<li class='w150px'>"+response.sti_nm+"<input type='hidden' value='"+response.sti_cd+"' /></li>";
                   getDfctInf += "<li class='w110px'>"+response.plm_cdt+"</li>";
                   getDfctInf += "<li class='w150px'>"+response.rpt_rst_acttm_nm+"<input type='hidden' value='"+response.rpt_rst_acttm+"' /></li>";
                   getDfctInf += "<li class='w100px'>"+response.rpt_usrnm+"</li>";
                   getDfctInf += "<li class='w80px tAlgnCntr'>"+response.rtnsec+"</li>";
                   getDfctInf += "<li class='w80px tAlgnCntr'><button class='inFileBtn'>"+response.file_yn+"</button></li>";
                   getDfctInf += "<li class='dsplyNon _rpt_astdesc'>"+response.rpt_astdesc+"</li>";
                   getDfctInf += "<li class='dsplyNon _rpt_desc'>"+response.rpt_desc+"</li>";
                   getDfctInf += "<li class='dsplyNon _opinion'>"+response.opinion+"</li>";
                   getDfctInf += "</ul>";
                $('#getDfctInf').append(getDfctInf);
                inFileBtnPop();
                $('.getDfctInfLst').click(function() {
                   $('.ulLftlst').removeClass('on');
                   var rpt_astdesc = $(this).find('._rpt_astdesc').text();
                   var rpt_desc =  $(this).find('._rpt_desc').text();
                   var opinion =  $(this).find('._opinion').text();
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
                     if(opinion.length > 0){
                        $('#opinion').text(opinion);
                     }else{
                        $('#opinion').text('이의 제기내용이 없습니다.');
                     }
         	           $(this).addClass('on');
                });
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getMglSubInf() {//상세상세미결현황요약정보
     $('#getMglSubInf').find('.ulLftlst._data').remove();
     $.ajax({
       url: "/v1/api/tmserp/getMigyeoDetaillnfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                plm_no: 'I202110202227'
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getMglSubInf = "<ul class='ulLftlst _data'>";
                   getMglSubInf += "<li class='w60px _lstNum'></li>";
                   getMglSubInf += "<li class='w150px'>"+response.com_pldsec+"</li>";
                   getMglSubInf += "<li class='w150px'>"+response.itm_cd+"</li>";
                   getMglSubInf += "<li class='w150px'>"+response.col_cd+"</li>";
                   getMglSubInf += "<li class='w250px'><!--단품명변수--></li>";
                   getMglSubInf += "<li class='w100px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value='"+response.pld_eqty+"' class='innmbr nmCmma w100p'></li>";
                   getMglSubInf += "<li class='w150px tAlgnRght'><span class='numTxt'></span><input type='text' name='' value='"+response.pld_famt+"' class='innmbr nmCmma w100p'></li>";
                   getMglSubInf += "<li class='w150px'>"+response.com_updsec+"</li>";
                   getMglSubInf += "<li class='w300px'>"+response.pld_rmk+"</li>";
                   getMglSubInf += "</ul>";
                $('#getMglSubInf').append(getMglSubInf);
                   cmma();//콤마
                   innmbr();//인풋값 스팬으로 넘기기
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getMigyeolInfo() {//상세미결현황요약정보
     $('.migyeolInfo').addClass('opn');
     $.ajax({
       url: "/v1/api/tmserp/getMigyeolnfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                from_dt: '20210910',
                to_dt: '20211020',
                com_scd:'C16YA',
                ksti_cd:'YA601',
                sti_cd: 'YA601'
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var mglRprtInf = "<ul class='ulLftlst _inmigyeolInf'>";
                   mglRprtInf += "<li class='w150px'><a class='lnk' onclick='getMglSubInf()'>"+response.sti_nm+"</a></li>";
                   mglRprtInf += "<li class='w100px'>"+response.com_ssec+"<input type='hidden' name='plm_no' value='"+response.plm_no+"' /></li>";
                   mglRprtInf += "<li class='w110px'>"+response.rem_dt+"</li>";
                   mglRprtInf += "<li class='w100px'>"+response.com_brand+"</li>";
                   mglRprtInf += "<li class='w150px'>"+response.agt_nm+"</li>";
                   mglRprtInf += "<li class='w200px'>"+response.orm_nm+"</li>";
                   mglRprtInf += "<li class='w150px'>"+response.orm_no+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr _unpsecTxt'>"+response.unpsec_r_yn+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr _unpsecTxt'>"+response.unpsec_a_yn+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr _unpsecTxt'>"+response.unpsec_e_yn+"</li>";
                   mglRprtInf += "<li class='w100px tAlgnCntr _unpsecTxt'>"+response.unpsec_c_yn+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr'><button class='inFileBtn'>"+response.file_yn+"</button></li>";
                   mglRprtInf += "<li class='w300px'>"+response.mob_rmk+"</li>";
                   mglRprtInf += "</ul>";
                $('#getMigyeolInf').append(mglRprtInf);
                migyeolinfStl() ;//미경상세화면변경
                inFileBtnPop();
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getMigyeolReportInfo() {//미결현황요약정보
     $.ajax({
       url: "/v1/api/tmserp/getMigyeolReportInfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                from_dt: '20210910',
                to_dt:'20211020',
                com_scd:'C16YA',
                ksti_cd:'YA601'
       },
       success: function(list){
            $.each(list, function(idx, response) {
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
               var getMglRprtInfLst = "<ul class='ulLftlst' onclick='getMigyeolInfo()'>";
                   getMglRprtInfLst += "<li class='w150px'>"+response.sti_nm+"<input type='hidden' name='sti_cd' value='"+response.sti_cd+"' /></li>";
                   getMglRprtInfLst += "<li class='w100px tAlgnCntr fnt500'>"+response.tot_cnt+"</li>";
                   getMglRprtInfLst += "<li class='w100px'>"+response.comp_cnt+" ("+response.comp_per+"%)</li>";
                   getMglRprtInfLst += "<li class='w100px'>"+response.migyeol_cnt+" ("+response.migyeol_per+"%)</li>";
                   getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_r'>"+response.com_unpsec_r+" ("+response.com_unpsec_r_per+"%)</li>";
                   getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_a'>"+response.com_unpsec_a+" ("+response.com_unpsec_a_per+"%)</li>";
                   getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_e'>"+response.com_unpsec_e+" ("+response.com_unpsec_e_per+"%)</li>";
                   getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_c'>"+response.com_unpsec_c+" ("+response.com_unpsec_c_per+"%)</li>";
                   getMglRprtInfLst += "</ul>";
                $('#getMigyeolRprtInfLst').append(getMglRprtInfLst);
               migyeolPerBar();
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}

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
                  /*$('.vndBSsBtn').each(function() {
                  var vndBSsBtntxt = $(this).text();
                     if(vndBSsBtntxt == "완결"){
                          $(this).addClass('non');
                     }else{
                          $(this).removeClass('non');
                     }
                });*/
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
     $('#cnstrctLst_dtlInf').find('.ulLftlst._data').remove();
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

			    var cnstrctLst_dtlInf = "<ul class='ulLftlst _data'>";
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
