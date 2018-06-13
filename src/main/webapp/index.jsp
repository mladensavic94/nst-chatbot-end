<%-- 
    Document   : index
    Created on : Nov 27, 2017, 11:06:25 PM
    Author     : Mladen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Chatbot landing page</title>
        <jsp:include page="WEB-INF/include.jsp" />
    </head>
    <body>
    <div class="main-content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="header">
                            <h4 class="title">REST API SPECIFICATION</h4>
                        </div>
                        <div class="content table-responsive table-full-width">
                            <table class="table table-hover table-striped table-responsive">
                                <thead >
                                <tr style="max-width: 100px;">
                                    <th class="text-center">Putanja</th>
                                    <th class="text-center">Metod</th>
                                    <th class="text-center">Opis</th>
                                </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td class="text-center">/login</td>
                                        <td class="text-center">GET</td>
                                        <td class="text-center">login putanja - prihvata email i password i vraca token ukoliko je uspesna autentikacija</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/register</td>
                                        <td class="text-center">POST</td>
                                        <td class="text-center">Registracija novog profesora - email, password, ime i prezime</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/professors</td>
                                        <td class="text-center">GET</td>
                                        <td class="text-center">Vraca sve podatke o profesorima - token</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/professor</td>
                                        <td class="text-center">GET</td>
                                        <td class="text-center">Vraca sve podatke o profesoru - email, token</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/professor/save</td>
                                        <td class="text-center">POST</td>
                                        <td class="text-center">Vraca sve podatke o profesoru - email, token</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/chatbotVerification</td>
                                        <td class="text-center">GET</td>
                                        <td class="text-center">Facebook messenger verification</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/chatbot</td>
                                        <td class="text-center">POST</td>
                                        <td class="text-center">Facebook messenger komunikacioni kanal</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/officehours</td>
                                        <td class="text-center">GET</td>
                                        <td class="text-center">Termini konsultacija za profesora - email, token</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/officehours</td>
                                        <td class="text-center">POST</td>
                                        <td class="text-center">Dodavanje termina konsultacija - termin, token</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/appointments</td>
                                        <td class="text-center">GET</td>
                                        <td class="text-center">Svi zakazani termini za profesora - email, token</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/appointments/save</td>
                                        <td class="text-center">POST</td>
                                        <td class="text-center">Dodavanje novog zahteva - zahtev, token</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/appointments/update</td>
                                        <td class="text-center">POST</td>
                                        <td class="text-center">Izmena zahteva - zahtev, token</td>
                                    </tr><tr>
                                        <td class="text-center">/rest/appointments/filter</td>
                                        <td class="text-center">GET</td>
                                        <td class="text-center">Filtriranje zahteva po terminu i profesoru - termin id, profesor, token</td>
                                    </tr>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    </body>
</html>

